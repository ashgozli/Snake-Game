import javax.swing.*;
import javax.xml.crypto.dsig.spec.DigestMethodParameterSpec;
import java.awt.*;
import java.awt.event.*;
import java.nio.channels.FileLock;
import java.util.Random;
import java.util.Random.*;
import java.util.Scanner;


public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 1000;
    static final int SCREEN_HEIGHT = 1000;
    static final int UNIT_SIZE = 25; //How large the objects are.
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;

    // coordinates for all the snake body parts!
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6; //6 body parts to start with


    // Apple positioning initialization
    int applesEaten;
    int appleX;
    int appleY;

    char direction = 'R'; //Snake begins by going to right
    boolean running = false;

    Timer timer;
    Random random;


    //Constructor
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();


    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);
        draw(g);



    }

    public void draw(Graphics g){


        // Optional Grid Lines to better visualize the game's mapping
        for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++){
            g.drawLine(i*UNIT_SIZE, 0 , i*UNIT_SIZE , SCREEN_HEIGHT);
            g.drawLine(0 , i*UNIT_SIZE , SCREEN_WIDTH , i*UNIT_SIZE);
        }

        if(running) {
            //Draw Apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); //Fill the grids randomly with respect to the UNIT_SIZE


            //Draw Snake (Head & Body)
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    // If bodypart is the head then color green takes place
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    // If bodypart is not the head then another color takes place for body
                    //g.setColor(new Color(45, 180, 13));
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                g.setColor(Color.red);
                g.setFont(new Font("Ink Free", Font.BOLD, 35));
                //Font Matrics (Centre)
                FontMetrics metrics = getFontMetrics(g.getFont());

                g.drawString("Score: "+applesEaten , (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2 , g.getFont().getSize() );
            }

        }

        else {
            gameOver(g);

        }



    }

    public void newApple(){
        appleX = random.nextInt((int) (SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE; //Apples on the x coord
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE; //Apples on the y coord



    }

    public void move() {

        //Snake move
        for( int i = bodyParts; i > 0 ; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];

        }



        // Establishing our directions using the x&y coordinates
        switch (direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }

    public void checkApple() {

        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }

    }

    public void checkCollisions(){

        // Check if head of snake collides
        // with the body to stop the game
        for (int i = bodyParts; i>0 ; i--){
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // Check if head touches borders

        // Left
        if(x[0] < 0){
            running = false;
        }

        //Right
        if(x[0] > SCREEN_WIDTH){
            running = false;
        }

        //Top
        if(y[0] < 0){
            running = false;
        }

        //Bottom
        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }

        if(!running){
            timer.stop();
        }
    }

    public void gameOver(Graphics g){

        //Score Text @ Game Over
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 35));
        //Font Matrics (Score When Game Over)
        FontMetrics metrics1 = getFontMetrics(g.getFont());

        g.drawString("Score: "+applesEaten , (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2 , g.getFont().getSize() );



        //Game Over Text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        //Font Matrics (Centre)
        FontMetrics metrics2 = getFontMetrics(g.getFont());

        g.drawString("You Lost :)" , (SCREEN_WIDTH - metrics2.stringWidth("You Lost"))/2 , SCREEN_HEIGHT/2 );

    }

    @Override
    public void actionPerformed(ActionEvent e){

        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    //inner Class
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                if(direction != 'L'){
                    direction = 'R';
                }
                break;

                case KeyEvent.VK_UP:
                if(direction != 'D'){
                    direction = 'U';
                }
                break;

                case KeyEvent.VK_DOWN:
                if(direction != 'U'){
                    direction = 'D';
                }
                break;
            }
        }

    }



}

