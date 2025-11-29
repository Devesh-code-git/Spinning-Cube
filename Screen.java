import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;
import java.util.Arrays;

public class Screen extends JPanel implements ActionListener{
    int d = 600;
    Color neon_blue = new Color(31, 81, 255);
    Color neon_red = new Color(255, 49, 49);
    Color neon_green = new Color(57, 255, 20);
    Color neon_orange = new Color(255, 81, 31);
    Color dark_grey = new Color(36, 36, 36);
    double[][] base_points = {{-100, -100, -100}, // P1
                              {-100, -100, 100},  // P2 
                              {-100, 100, -100},  // P3
                              {-100, 100, 100},   // P4
                              {100, -100, -100},  // P5
                              {100, -100, 100},   // P6
                              {100, 100, -100},   // P7
                              {100, 100, 100}};   // P8
    double[][] points = new double[8][3];
    double A = 0.01;
    double B = 0.02;
    double C = 0.015;
    int screen_size = 800;
    Timer timer;
    double d1, d2, d3, d4, d5, d6, d7, d8, face1_total, face2_total, face3_total, face4_total, face5_total, face6_total;
    double[] order = {0, 0, 0, 0, 0, 0};

    public static void main(String[] args) {
        new Frame();
    }

    public Screen() {
        this.setPreferredSize(new Dimension(screen_size, screen_size));
        this.setBackground(dark_grey);
        this.setLayout(null);

        timer = new Timer(20, this);
        timer.start();
    }

    // Rotate about x-axis
    public void rotate_X(double[] P) {
        double x = P[0];
        double y = P[1]*Math.cos(A) - P[2]*Math.sin(A);
        double z = P[1]*Math.sin(A) + P[2]*Math.cos(A);

        P[0] = x;
        P[1] = y;
        P[2] = z;
    }

    // Rotate about y-axis
    public void rotate_Y(double[] P) {
        double x = P[0]*Math.cos(B) + P[2]*Math.sin(B);
        double y = P[1];
        double z = -P[0]*Math.sin(B) + P[2]*Math.cos(B);

        P[0] = x;
        P[1] = y;
        P[2] = z;
    }

    // Rotate about z-axis
    public void rotate_Z(double[] P) {
        double x = P[0]*Math.cos(C) - P[1]*Math.sin(C);
        double y = P[0]*Math.sin(C) + P[1]*Math.cos(C);
        double z = P[2];

        P[0] = x;
        P[1] = y;
        P[2] = z;
    }

    public int update_coordinates(double n, double z) {
        return (int)((n * d) / (d - z)); // Focal length formula
    }

    public void distance(double[] P1, double[] P2, double[] P3, double[] P4, double[] P5, double[] P6, double[] P7, double[] P8) {
        // Calculating distance for all points from refrence points
        d1 = Math.sqrt(Math.pow((0 - P1[0]), 2) + Math.pow((0 - P1[1]), 2) + Math.pow((1400 - P1[2]), 2));
        d2 = Math.sqrt(Math.pow((0 - P2[0]), 2) + Math.pow((0 - P2[1]), 2) + Math.pow((1400 - P2[2]), 2));
        d3 = Math.sqrt(Math.pow((0 - P3[0]), 2) + Math.pow((0 - P3[1]), 2) + Math.pow((1400 - P3[2]), 2));
        d4 = Math.sqrt(Math.pow((0 - P4[0]), 2) + Math.pow((0 - P4[1]), 2) + Math.pow((1400 - P4[2]), 2));
        d5 = Math.sqrt(Math.pow((0 - P5[0]), 2) + Math.pow((0 - P5[1]), 2) + Math.pow((1400 - P5[2]), 2));
        d6 = Math.sqrt(Math.pow((0 - P6[0]), 2) + Math.pow((0 - P6[1]), 2) + Math.pow((1400 - P6[2]), 2));
        d7 = Math.sqrt(Math.pow((0 - P7[0]), 2) + Math.pow((0 - P7[1]), 2) + Math.pow((1400 - P7[2]), 2));
        d8 = Math.sqrt(Math.pow((0 - P8[0]), 2) + Math.pow((0 - P8[1]), 2) + Math.pow((1400 - P8[2]), 2));

        face1_total = (d2 + d4 + d6 + d8) / 4;
        face2_total = (d5 + d6 + d7 + d8) / 4;
        face3_total = (d1 + d3 + d5 + d7) / 4;
        face4_total = (d1 + d2 + d3 + d4) / 4;
        face5_total = (d1 + d2 + d5 + d6) / 4;
        face6_total = (d3 + d4 + d7 + d8) / 4;

        order[0] = face1_total;
        order[1] = face2_total;
        order[2] = face3_total;
        order[3] = face4_total;
        order[4] = face5_total;
        order[5] = face6_total;

        Arrays.sort(order);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        super.paintComponent(g);
        g2D.translate(getWidth() / 2, getHeight() / 2);
        g2D.scale(1, -1);

        for (int i = order.length - 1; i >= 0; i--) {
            if (order[i] == face1_total) {
                g2D.setColor(neon_blue);
                Path2D face1 = new Path2D.Double();

                face1.moveTo(update_coordinates(points[1][0], points[1][2]), update_coordinates(points[1][1], points[1][2]));   
                face1.lineTo(update_coordinates(points[3][0], points[3][2]), update_coordinates(points[3][1], points[3][2]));  
                face1.lineTo(update_coordinates(points[7][0], points[7][2]), update_coordinates(points[7][1], points[7][2]));
                face1.lineTo(update_coordinates(points[5][0], points[5][2]), update_coordinates(points[5][1], points[5][2])); 

                face1.closePath(); // Drawing face
                g2D.fill(face1);

                g2D.setColor(Color.WHITE);
                g2D.setStroke(new BasicStroke(3));

                // Drawing outline
                g2D.drawLine(update_coordinates(points[1][0], points[1][2]), update_coordinates(points[1][1], points[1][2]), 
                update_coordinates(points[3][0], points[3][2]), update_coordinates(points[3][1], points[3][2]));

                g2D.drawLine(update_coordinates(points[3][0], points[3][2]), update_coordinates(points[3][1], points[3][2]), 
                update_coordinates(points[7][0], points[7][2]), update_coordinates(points[7][1], points[7][2]));

                g2D.drawLine(update_coordinates(points[7][0], points[7][2]), update_coordinates(points[7][1], points[7][2]), 
                update_coordinates(points[5][0], points[5][2]), update_coordinates(points[5][1], points[5][2]));

                g2D.drawLine(update_coordinates(points[5][0], points[5][2]), update_coordinates(points[5][1], points[5][2]), 
                update_coordinates(points[1][0], points[1][2]), update_coordinates(points[1][1], points[1][2]));
            } else if (order[i] == face2_total) {
                g2D.setColor(neon_orange);
                Path2D face2 = new Path2D.Double();

                face2.moveTo(update_coordinates(points[4][0], points[4][2]), update_coordinates(points[4][1], points[4][2]));   
                face2.lineTo(update_coordinates(points[5][0], points[5][2]), update_coordinates(points[5][1], points[5][2]));  
                face2.lineTo(update_coordinates(points[7][0], points[7][2]), update_coordinates(points[7][1], points[7][2]));
                face2.lineTo(update_coordinates(points[6][0], points[6][2]), update_coordinates(points[6][1], points[6][2]));

                face2.closePath(); // Drawing face
                g2D.fill(face2);

                g2D.setColor(Color.WHITE);
                g2D.setStroke(new BasicStroke(3));

                // Drawing outline
                g2D.drawLine(update_coordinates(points[4][0], points[4][2]), update_coordinates(points[4][1], points[4][2]), 
                update_coordinates(points[6][0], points[6][2]), update_coordinates(points[6][1], points[6][2]));

                g2D.drawLine(update_coordinates(points[6][0], points[6][2]), update_coordinates(points[6][1], points[6][2]), 
                update_coordinates(points[7][0], points[7][2]), update_coordinates(points[7][1], points[7][2]));

                g2D.drawLine(update_coordinates(points[4][0], points[4][2]), update_coordinates(points[4][1], points[4][2]), 
                update_coordinates(points[5][0], points[5][2]), update_coordinates(points[5][1], points[5][2]));
            } else if (order[i] == face3_total) {
                g2D.setColor(neon_green);
                Path2D face3 = new Path2D.Double();

                face3.moveTo(update_coordinates(points[0][0], points[0][2]), update_coordinates(points[0][1], points[0][2]));   
                face3.lineTo(update_coordinates(points[2][0], points[2][2]), update_coordinates(points[2][1], points[2][2]));  
                face3.lineTo(update_coordinates(points[6][0], points[6][2]), update_coordinates(points[6][1], points[6][2]));
                face3.lineTo(update_coordinates(points[4][0], points[4][2]), update_coordinates(points[4][1], points[4][2]));

                face3.closePath(); // Drawing face
                g2D.fill(face3);

                g2D.setColor(Color.WHITE);
                g2D.setStroke(new BasicStroke(3));

                // Drawing outline
                g2D.drawLine(update_coordinates(points[0][0], points[0][2]), update_coordinates(points[0][1], points[0][2]), 
                update_coordinates(points[2][0], points[2][2]), update_coordinates(points[2][1], points[2][2]));

                g2D.drawLine(update_coordinates(points[2][0], points[2][2]), update_coordinates(points[2][1], points[2][2]), 
                update_coordinates(points[6][0], points[6][2]), update_coordinates(points[6][1], points[6][2]));

                g2D.drawLine(update_coordinates(points[0][0], points[0][2]), update_coordinates(points[0][1], points[0][2]), 
                update_coordinates(points[4][0], points[4][2]), update_coordinates(points[4][1], points[4][2]));
            } else if (order[i] == face4_total) {
                g2D.setColor(neon_red);
                Path2D face4 = new Path2D.Double();

                face4.moveTo(update_coordinates(points[0][0], points[0][2]), update_coordinates(points[0][1], points[0][2]));   
                face4.lineTo(update_coordinates(points[1][0], points[1][2]), update_coordinates(points[1][1], points[1][2]));  
                face4.lineTo(update_coordinates(points[3][0], points[3][2]), update_coordinates(points[3][1], points[3][2]));
                face4.lineTo(update_coordinates(points[2][0], points[2][2]), update_coordinates(points[2][1], points[2][2]));

                face4.closePath(); // Drawing face
                g2D.fill(face4);

                g2D.setColor(Color.WHITE);
                g2D.setStroke(new BasicStroke(3));

                // Drawing outline
                g2D.drawLine(update_coordinates(points[0][0], points[0][2]), update_coordinates(points[0][1], points[0][2]), 
                update_coordinates(points[1][0], points[1][2]), update_coordinates(points[1][1], points[1][2]));

                g2D.drawLine(update_coordinates(points[2][0], points[2][2]), update_coordinates(points[2][1], points[2][2]), 
                update_coordinates(points[3][0], points[3][2]), update_coordinates(points[3][1], points[3][2]));
            } else if (order[i] == face5_total) {
                g2D.setColor(Color.YELLOW);
                Path2D face5 = new Path2D.Double();

                face5.moveTo(update_coordinates(points[0][0], points[0][2]), update_coordinates(points[0][1], points[0][2]));   
                face5.lineTo(update_coordinates(points[1][0], points[1][2]), update_coordinates(points[1][1], points[1][2]));  
                face5.lineTo(update_coordinates(points[5][0], points[5][2]), update_coordinates(points[5][1], points[5][2]));
                face5.lineTo(update_coordinates(points[4][0], points[4][2]), update_coordinates(points[4][1], points[4][2])); 

                face5.closePath(); // Drawing face
                g2D.fill(face5);
            } else {
                g2D.setColor(Color.WHITE);
                Path2D face6 = new Path2D.Double();

                face6.moveTo(update_coordinates(points[2][0], points[2][2]), update_coordinates(points[2][1], points[2][2]));   
                face6.lineTo(update_coordinates(points[3][0], points[3][2]), update_coordinates(points[3][1], points[3][2]));  
                face6.lineTo(update_coordinates(points[7][0], points[7][2]), update_coordinates(points[7][1], points[7][2]));
                face6.lineTo(update_coordinates(points[6][0], points[6][2]), update_coordinates(points[6][1], points[6][2]));  

                face6.closePath(); // Drawing face
                g2D.fill(face6);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // If angles are greater than or equal to 2π reset
        if (A >= 6.28) {A = 0.01;}
        if (B >= 6.28) {B = 0.02;}
        if (C >= 6.28) {C = 0.015;}

        // Making rotated points back into base points
        for (int i = 0; i < base_points.length; i++) {
            points[i][0] = base_points[i][0];
            points[i][1] = base_points[i][1];
            points[i][2] = base_points[i][2];
        }

        // Rotating all points
        for (int i = 0; i < points.length; i++) {
            rotate_X(points[i]);
            rotate_Y(points[i]);
            rotate_Z(points[i]);
        }

        distance(points[0], points[1], points[2], points[3], points[4], points[5], points[6], points[7]);

        // Accumulating Angles
        A += 0.01;
        B += 0.02;
        C += 0.015;

        repaint();
    }
}
