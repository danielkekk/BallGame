import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*; 
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Game {

    static final int NUMBER_OF_BALLS=20;
    static final int WIDTH=800;
    static final int HEIGHT=600;

    private boolean stopped = false;
    private int elapsedTimeMillisec = 0;
    private int lastPoints;


    private ArrayList<Ball> balls;
    private int numOfActiveBalls;

    private int xPos;
    private int yPos;

    public Game() {

        BufferedReader bufferedReader = null;

        try {

            File file = new File("points.txt");
            FileReader fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                this.lastPoints = Integer.parseInt(line);
            }
            bufferedReader.close();

        } catch(IOException ex) {
            ex.printStackTrace();
        } catch(NumberFormatException ex) {
            this.lastPoints=0;
        } finally {
            try {
                bufferedReader.close();
            } catch(IOException  ex) {
                ex.printStackTrace();
            }
        }

        
        xPos=200;
        yPos=150;

        balls = new ArrayList<>();
        for(int i=0; i<Game.NUMBER_OF_BALLS; i++) {
            balls.add(new Ball());
        }
        balls.get(0).setActive(true);
        this.numOfActiveBalls = 1;
    }
    
    public void go() {

        JFrame frame = new JFrame("Ball Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MyDrawPanel drawPanel = new MyDrawPanel();

        JLabel clockLabel = new JLabel("0");
        Dimension dim = drawPanel.getSize();

        JButton startBtn = new JButton("Start");
        startBtn.addActionListener(e -> setStopped(false));
        JButton resetBtn = new JButton("Reset");
        resetBtn.addActionListener(e -> {
            this.setBallsDefault(); elapsedTimeMillisec=0;
            clockLabel.setText(Float.toString(elapsedTimeMillisec)+"\nLAST: "+Integer.toString(this.lastPoints));
            drawPanel.repaint();
        });
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        btnPanel.add(clockLabel);
        btnPanel.add(startBtn);
        btnPanel.add(resetBtn);

        frame.getContentPane().add(drawPanel);
        frame.getContentPane().add(BorderLayout.EAST, btnPanel);
        frame.setSize(Game.WIDTH, Game.HEIGHT);
        frame.setVisible(true);

        outerloop:
        while(true) {
            if(!isStopped()) {

                List<Ball> activeBalls = this.getActiveBalls();
                for(Ball activeBall: activeBalls) {
                    activeBall.pushBall();
                    activeBall.pullBall();

                    if((activeBall.getYPos()+80)>=Game.HEIGHT) {
                        activeBall.turnYVector();
                    }
                    if(activeBall.getYPos()<=0) {
                        activeBall.turnYVector();
                    }
                    if((activeBall.getXPos()+80)>=Game.WIDTH) {
                        activeBall.turnXVector();
                    }
                    if(activeBall.getXPos()<=0) {
                        activeBall.turnXVector();
                    }
                    if(activeBall.isCollided(new Point(this.xPos,this.yPos),new Dimension(30,30))) {
                        setStopped(true);

                        FileWriter writer = null;
                        try {

                            writer = new FileWriter("points.txt");
                            writer.write(Integer.toString(Math.round(elapsedTimeMillisec/1000)));
                            writer.close();

                            clockLabel.setText("GAME OVER! YOUR POINTS:" + Float.toString(elapsedTimeMillisec/1000));

                        } catch(IOException ex) {
                            ex.printStackTrace();
                        } finally {
                            try {
                                writer.close();
                            } catch(IOException  ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }

                elapsedTimeMillisec += 25;
                if((elapsedTimeMillisec % 1000) == 0) {
                    clockLabel.setText(Float.toString(elapsedTimeMillisec/1000)+"\nLAST: "+Integer.toString(this.lastPoints));
                }
                if((elapsedTimeMillisec % 10000) == 0) {
                    if(this.numOfActiveBalls == Game.NUMBER_OF_BALLS) {
                        break;
                    }
                    balls.get(numOfActiveBalls).setActive(true);
                    this.numOfActiveBalls++;
                }

                drawPanel.repaint();
            }

            try {
                TimeUnit.MILLISECONDS.sleep(25);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setBallsDefault() {

        for(Ball ball: balls) {
            ball.setBallDefault();
        }

        balls.get(0).setActive(true);
    }

    public List<Ball> getActiveBalls() {
        List<Ball> activeBalls = this.balls.stream()
                                           .filter(tmpBall -> tmpBall.isActive())
                                           .collect(Collectors.toList());

        return activeBalls;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public boolean isStopped() {
        return this.stopped;
    }

    class MyDrawPanel extends JPanel implements KeyListener {

        public MyDrawPanel()
        {
            addKeyListener(this);
            setFocusable(true);
            setFocusTraversalKeysEnabled(false);
        }

        public void paintComponent(Graphics g) {
            g.setColor(Color.white);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());

            g.setColor(Color.pink);
            g.fillRect(Game.this.xPos, Game.this.yPos, 30, 30);

            List<Ball> activeBalls = getActiveBalls();
            for(Ball activeBall: activeBalls) {
                g.setColor(activeBall.getColor());
                g.fillOval(activeBall.getXPos(), activeBall.getYPos(), Ball.BALL_WIDTH, Ball.BALL_HEIGHT);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) { 
            int keyCode = e.getKeyCode(); 

            if(!Game.this.isStopped()) {
                if(keyCode==38) {
                    Game.this.yPos -= 10;
                } else if(keyCode==40) {
                    Game.this.yPos += 10;
                } else if(keyCode==37) {
                    Game.this.xPos -= 10;
                } else if(keyCode==39) {
                    Game.this.xPos += 10;
                }
            }
        }
    
        @Override
        public void keyReleased(KeyEvent e) { 
            //todo
        }

        @Override
        public void keyTyped(KeyEvent e) {
            //System.out.println("Key Typed: " + e.getKeyChar());
        }
    }
}

