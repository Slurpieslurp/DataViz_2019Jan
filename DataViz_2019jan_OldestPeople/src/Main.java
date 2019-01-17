import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private ArrayList<Person> people = new ArrayList<>();
    private final Color BG_COLOR      = new Color(250,250,250);
    private final Color UNDER_COLOR   = new Color(240, 240, 240);
    private final Color VGRID_COLOR   = new Color(150, 150, 150);
    private final Color HGRID_COLOR   = new Color(210, 210, 210);
    private final Color TEXT_COLOR    = new Color(65, 65, 65);
    private final Color TEXT_COLOR2   = new Color(150, 150, 150);
    private final Color COLOR_FEMALE  = new Color(255,86,196);
    private final Color COLOR_MALE    = new Color(28,168,255);
    private final Color COLOR_W       = new Color(78, 186, 88);
    private final Color COLOR_B       = new Color(146, 77, 183);
    private final Color COLOR_EA      = new Color(226,184,45);
    private final Color COLOR_H       = new Color(239, 134, 4);
    private final Color COLOR_M       = new Color(150, 150, 150);
    private final int LINE_WIDTH      = 7;
    private final int GRID_LINE_WIDTH = 1;
    private final int AGE_CUTOFF      = 100;
    private final int DOT_SIZE        = (int) (2 * LINE_WIDTH);
    private final int GRADIENT_SIZE   = 10 * LINE_WIDTH;
    private final int LEGEND_LINE_LEN = 3 * DOT_SIZE;
    private final int LARGE_FONT_SIZE = 28;
    private final int SMALL_FONT_SIZE = 20;
    private final int TITLE_FONT_SIZE = 64;
    private final int AXES_TITLE_FONT_SIZE = 40;
    private final int AXES_VALUE_FONT_SIZE = 24;

    private final int BOTTOM_PADDING     = 150;
    private final int LEFT_PADDING       = 150;
    private final int TOP_PADDING        = 250;
    private final int RIGHT_PADDING      = 400;
    private final int HORIZONTAL_PADDING = LEFT_PADDING + RIGHT_PADDING;
    private final int VERTICAL_PADDING   = TOP_PADDING  + BOTTOM_PADDING;

    public static void main(String[] args) {
        (new Main()).run();
    }

    private void run() {
        readInput();

        String firstDateDied = people.get(0).getDateDied();
        String lastDateDied  = people.get(people.size() - 1).getDateDied();
        int startYear = Integer.parseInt(firstDateDied.substring(firstDateDied.length() - 4));
        int endYear   = 2018;

        TimeSpan startStamp    = new TimeSpan(startYear, 0);
        TimeSpan totalTimeSpan = new TimeSpan(endYear - startYear + 1, 0);
        int totalDays = timeSpanToDays(startStamp, totalTimeSpan).getDays();

        makeGraph(startStamp, totalDays);
    }

    private void readInput() {
        try {
            Scanner sc = new Scanner (new FileReader("data.csv"));
            String line;
            String[] lineElements;
            Person person;

            // Ignore the first line
            line = sc.nextLine();

            while (sc.hasNextLine()) {
                // Read and process line
                line = sc.nextLine();
                lineElements = line.split(";");

                // Define new person based on input
                person = new Person(lineElements[1], lineElements[2], lineElements[3], lineElements[4],
                        Integer.parseInt(lineElements[5]), Integer.parseInt(lineElements[6]), lineElements[7],
                        lineElements[8], lineElements[9], lineElements[10], lineElements[11],
                        Integer.parseInt(lineElements[12]), Integer.parseInt(lineElements[13]),
                        Float.valueOf(lineElements[14]), Integer.parseInt(lineElements[15]),
                        Integer.parseInt(lineElements[16]));

                people.add(person);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void makeGraph(TimeSpan timeStamp, int totalDays) {
        int YEAR_WIDTH  = 60;
        int YEAR_HEIGHT = 30;

        int width  = totalDays * YEAR_WIDTH / 365 + HORIZONTAL_PADDING;
        int height = (maxAge().getYears() - AGE_CUTOFF + 1) * YEAR_HEIGHT + VERTICAL_PADDING;
        int graphBottom = height - BOTTOM_PADDING;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        AffineTransform orig = g.getTransform();

        // Fill image with background color
        g.setColor(BG_COLOR);
        g.fillRect(0, 0, width, height);


        // Draw vertcial axis values, ticks and lines
        g.setFont(new Font("Century Gothic", Font.BOLD, AXES_VALUE_FONT_SIZE));
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g.setStroke(new BasicStroke(1));
        for (int i = 0; i < 125 - 100; i += 1) {
            String string = Integer.toString(100 + i);
            int stringWidth = g.getFontMetrics().stringWidth(string);
            int x = LEFT_PADDING - stringWidth - AXES_VALUE_FONT_SIZE;
            int y = graphBottom - i * YEAR_HEIGHT;

            if (i % 2 == 0) {
                g.setColor(TEXT_COLOR);
                g.drawString(string, x, y + g.getFontMetrics().getHeight() / 4);
            }

            g.setColor(HGRID_COLOR);
            g.drawLine(LEFT_PADDING, y, width - RIGHT_PADDING, y);
        }


        // Use anti-aliasing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);


        float timePassed = 0;
        int   x1;
        int   y1;
        int   x2;
        int   y2;
        people.get(0).setReignLength((float) 297 / 365);
        people.get(0).setAgeAtAccession(new TimeSpan(112, 133));

        Color[] gradient;
        g.setStroke(new BasicStroke(LINE_WIDTH / 2));
        for (Person person : people) {
            x1 = LEFT_PADDING + (int) (timePassed * YEAR_WIDTH);
            y1 = graphBottom - (int) ((person.getAgeAtAccesionInYears() - AGE_CUTOFF) * YEAR_HEIGHT);
            x2 = LEFT_PADDING + (int) ((timePassed + person.getReignLength()) * YEAR_WIDTH);
            y2 = graphBottom - (int) ((person.getAgeAtAccesionInYears() - AGE_CUTOFF + person.getReignLength()) * YEAR_HEIGHT);

            g.setColor(UNDER_COLOR);
            g.fillPolygon(new int[]{x1, x1, x2, x2}, new int[]{graphBottom, y1, y2, graphBottom}, 4);

            switch (person.getSex()) {
                case "F":
                    gradient = linearGradient(COLOR_FEMALE, UNDER_COLOR, GRADIENT_SIZE);
                    break;
                case "M":
                    gradient = linearGradient(COLOR_MALE, UNDER_COLOR, GRADIENT_SIZE);
                    break;
                default:
                    gradient = linearGradient(Color.RED, Color.RED, GRADIENT_SIZE);
            }

            for (int i = 0; i < GRADIENT_SIZE; i++) {
                g.setColor(gradient[i]);
                g.drawLine(x1 + (int) ((BasicStroke) g.getStroke()).getLineWidth() - 1, y1 + i,
                           x2 - (int) ((BasicStroke) g.getStroke()).getLineWidth() + 1, y2 + i);
            }

            timePassed += person.getReignLength();
        }
        timePassed = 0;

        g.setStroke(new BasicStroke(GRID_LINE_WIDTH));
        for (Person person : people) {
            x1 = LEFT_PADDING + (int) (timePassed * YEAR_WIDTH);
            y1 = graphBottom - (int) ((person.getAgeAtAccesionInYears() - AGE_CUTOFF) * YEAR_HEIGHT);
            x2 = LEFT_PADDING + (int) ((timePassed + person.getReignLength()) * YEAR_WIDTH);
            y2 = graphBottom - (int) ((person.getAgeAtAccesionInYears() - AGE_CUTOFF + person.getReignLength()) * YEAR_HEIGHT);

            g.setColor(VGRID_COLOR);
            g.drawLine(x2, graphBottom, x2, y2);

            timePassed += person.getReignLength();
        }

        for (int i = people.size() - 1; i >= 0; i--) {
            Person person = people.get(i);
            timePassed -= person.getReignLength();

            x1 = LEFT_PADDING + (int) (timePassed * YEAR_WIDTH);
            y1 = graphBottom - (int) ((person.getAgeAtAccesionInYears() - AGE_CUTOFF) * YEAR_HEIGHT);
            x2 = LEFT_PADDING + (int) ((timePassed + person.getReignLength()) * YEAR_WIDTH);
            y2 = graphBottom - (int) ((person.getAgeAtAccesionInYears() - AGE_CUTOFF + person.getReignLength()) * YEAR_HEIGHT);


            switch (person.getRace()) {
                case "W":
                    g.setColor(COLOR_W);
                    break;
                case "B":
                    g.setColor(COLOR_B);
                    break;
                case "EA":
                    g.setColor(COLOR_EA);
                    break;
                case "H":
                    g.setColor(COLOR_H);
                    break;
                case "M":
                    g.setColor(COLOR_M);
                    break;
                default:
                    gradient = linearGradient(Color.RED, Color.RED, GRADIENT_SIZE);
            }

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.setStroke(new BasicStroke(LINE_WIDTH));
            g.drawLine(x1, y1, x2, y2);
            g.fillOval(x2 - DOT_SIZE / 2, y2 - DOT_SIZE / 2, DOT_SIZE, DOT_SIZE);


            String string = person.getAge().getYears() + "y " + person.getAge().getDays() + "d";
            int stringWidth  = g.getFontMetrics().stringWidth(person.getName());
            int stringHeight = g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent();

            if (x2 - x1 >= 0.8 * LARGE_FONT_SIZE) {
                g.rotate(-Math.PI / 2);
                g.setFont(new Font("Century Gothic", Font.BOLD, SMALL_FONT_SIZE));
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB);
                g.setColor(TEXT_COLOR2);

                g.drawString(string, -y2 + SMALL_FONT_SIZE,
                        x2 + stringHeight / 2);

                g.setTransform(orig);
            }
        }
        timePassed = 0;

        g.rotate(-Math.PI/2);
        g.setFont(new Font("Century Gothic", Font.BOLD, LARGE_FONT_SIZE));
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB);
        g.setColor(TEXT_COLOR);
        for (Person person : people) {
            x1 = LEFT_PADDING + (int) (timePassed * YEAR_WIDTH);
            y1 = graphBottom - (int) ((person.getAgeAtAccesionInYears() - AGE_CUTOFF) * YEAR_HEIGHT);
            x2 = LEFT_PADDING + (int) ((timePassed + person.getReignLength()) * YEAR_WIDTH);
            y2 = graphBottom - (int) ((person.getAgeAtAccesionInYears() - AGE_CUTOFF + person.getReignLength()) * YEAR_HEIGHT);

            int stringWidth  = g.getFontMetrics().stringWidth(person.getName());
            int stringHeight = g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent();
            if (x2 - x1 >= LARGE_FONT_SIZE && stringWidth + LARGE_FONT_SIZE < graphBottom - (y1 + (y2 - y1) / 2)) {
                g.drawString(person.getName(), - graphBottom + LARGE_FONT_SIZE / 2,
                        (x1 + (x2 - x1) / 2) + stringHeight / 2);
                /*g.drawString(person.getName(), height - stringWidth - FONT_SIZE,
                        -(x1 + (x2 - x1) / 2) + stringHeight / 2);*/
            }

            timePassed += person.getReignLength();
        }

        // Draw axes
        g.setTransform(orig);
        g.setStroke(new BasicStroke(3));
        g.setColor(TEXT_COLOR);
        g.drawLine(LEFT_PADDING - 2, (int) (0.8 * TOP_PADDING), LEFT_PADDING - 2, graphBottom + 2); // Vertical
        g.drawLine(LEFT_PADDING - 2, graphBottom + 2, width - RIGHT_PADDING, graphBottom + 2); // Horizontal

        // Draw horizontal axis title
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g.setFont(new Font("Century Gothic", Font.BOLD, AXES_TITLE_FONT_SIZE));
        g.drawString("TIME",
                LEFT_PADDING + (width - HORIZONTAL_PADDING) / 2 - g.getFontMetrics().stringWidth("TIME") / 2,
                height - g.getFontMetrics().getHeight());

        // Draw horizontal axis values and ticks
        g.setFont(new Font("Century Gothic", Font.BOLD, AXES_VALUE_FONT_SIZE));
        for (int i = 0; i < 2020 - 1955; i += 1) {
            String string = Integer.toString(1955 + i);
            int stringWidth = g.getFontMetrics().stringWidth(string);
            int x = LEFT_PADDING + i * YEAR_WIDTH;
            if (i % 5 == 0) {
                g.drawString(string, x - stringWidth / 2, graphBottom + (int) (2 * AXES_VALUE_FONT_SIZE));
                if (i > 0) {
                    g.fillRect(x - 1, graphBottom - 7, 3, 8);
                }
            } else {
                g.fillRect(x-1, graphBottom - 3, 3, 4);
            }
        }

        // Draw vertical axis title
        g.rotate(-Math.PI/2);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB);
        g.setFont(new Font("Century Gothic", Font.BOLD, AXES_TITLE_FONT_SIZE));
        g.drawString("AGE", - graphBottom + (height - VERTICAL_PADDING) / 2 - g.getFontMetrics().stringWidth("AGE") / 2,
                g.getFontMetrics().getHeight());
        g.setTransform(orig);

        // Draw title
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g.setFont(new Font("Century Gothic", Font.BOLD, TITLE_FONT_SIZE));
        String string = "Progression of the record for oldest person alive";
        int stringWidth = g.getFontMetrics().stringWidth(string);
        g.drawString(string, LEFT_PADDING + (width - HORIZONTAL_PADDING) / 2 - stringWidth / 2, TITLE_FONT_SIZE);
        string = "(1955 - 2018)";
        stringWidth = g.getFontMetrics().stringWidth(string);
        g.drawString(string, LEFT_PADDING + (width - HORIZONTAL_PADDING) / 2 - stringWidth / 2, 2*TITLE_FONT_SIZE);

        // Draw legend
        g.setFont(new Font("Century Gothic", Font.BOLD, AXES_VALUE_FONT_SIZE));
        string = "Race of record holder:";
        int x = width - RIGHT_PADDING + 3 * AXES_VALUE_FONT_SIZE;
        int y = TOP_PADDING;
        g.drawString(string, x, y);

        // Race lines
        g.setStroke(new BasicStroke(LINE_WIDTH));
        g.setFont(new Font("Century Gothic", Font.PLAIN, AXES_VALUE_FONT_SIZE));
        int stringHeight = g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent();
        for (int i = 1; i < 6; i++) {
            switch (i) {
                case 1:
                    string = "White";
                    g.setColor(COLOR_W);
                    break;
                case 2:
                    string = "East Asian";
                    g.setColor(COLOR_EA);
                    break;
                case 3:
                    string = "Black";
                    g.setColor(COLOR_B);
                    break;
                case 4:
                    string = "Hispanic";
                    g.setColor(COLOR_H);
                    break;
                case 5:
                    string = "Multiracial";
                    g.setColor(COLOR_M);
                    break;
            }

            y = (int) (TOP_PADDING + i * 1.5 * AXES_VALUE_FONT_SIZE);
            g.drawLine(x, y, x + LEGEND_LINE_LEN - DOT_SIZE / 2, y);
            g.fillOval(x + LEGEND_LINE_LEN - DOT_SIZE, y - DOT_SIZE / 2, DOT_SIZE, DOT_SIZE);
            g.setColor(TEXT_COLOR);
            g.drawString(string, x + LEGEND_LINE_LEN + DOT_SIZE, y + stringHeight / 2);
        }

        // Gender gradients
        g.setFont(new Font("Century Gothic", Font.BOLD, AXES_VALUE_FONT_SIZE));
        string = "Gender of record holder:";
        g.drawString(string, x, y += 6 * stringHeight);

        g.setFont(new Font("Century Gothic", Font.PLAIN, AXES_VALUE_FONT_SIZE));
        g.setStroke(new BasicStroke(1));
        for (int k = 1; k < 3; k++) {
            switch (k) {
                case 1:
                    string = "Female";
                    gradient = linearGradient(COLOR_FEMALE, BG_COLOR, (int) (stringHeight * 1.5));
                    break;
                case 2:
                    string = "Male";
                    gradient = linearGradient(COLOR_MALE, BG_COLOR, (int) (stringHeight * 1.5));
                    break;
                default:
                        gradient = linearGradient(Color.RED, Color.RED, (int) (stringHeight * 1.5));
            }

            y = (int) (y + ((2 - 0.25) * stringHeight));
            for (int i = 1; i < (int) (stringHeight * 1.5); i++) {
                g.setColor(gradient[i]);
                g.drawLine(x, y + i,x + LEGEND_LINE_LEN, y + i);
            }

            g.setColor(TEXT_COLOR);
            g.drawString(string, x + LEGEND_LINE_LEN + DOT_SIZE, y + stringHeight);

        }

        g.dispose();
        try {
            File imgfile = new File("chart.png");
            ImageIO.write(image,"png", imgfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private TimeSpanDaysPair timeSpanToDays(TimeSpan timeStamp, TimeSpan timeSpan) {
        int timeSpanYears = timeSpan.getYears();
        int timeSpanDays  = timeSpan.getDays();
        int days = 0;

        while (timeSpanYears > 0) {
            if (isLeapYear(timeStamp.getYears())) {
                days += 366;
            } else {
                days += 365;
            }

            timeStamp.incrementYears();
            timeSpanYears--;
        }

        while (timeSpanDays > 0) {
            days++;
            timeStamp.incrementDays();
            if ((timeStamp.getDays() >= 365 && !isLeapYear(timeStamp.getYears()) ) ||
                    timeStamp.getDays() >= 365 && isLeapYear(timeStamp.getYears())) {
                timeStamp.incrementYears();
                timeStamp.resetDays();
            } else {
                timeStamp.incrementDays();
            }
        }

        return new TimeSpanDaysPair(timeStamp, days);
    }

    private boolean isLeapYear(int year) {
        if (year % 400 == 0) {
            return true;
        } else if (year % 100 == 0) {
            return false;
        } else if (year % 4 == 0) {
            return true;
        } else {
            return false;
        }
    }

    private TimeSpan maxAge() {
        TimeSpan max = new TimeSpan(0,0);

        for (Person person : people) {
            if (person.getAge().getTotalDays() > max.getTotalDays()) {
                max = person.getAge();
            }
        }

        return max;
    }

    private Color[] linearGradient(Color start, Color end, int size) {
        Color[] gradient = new Color[size];
        int r1 = start.getRed();
        int g1 = start.getGreen();
        int b1 = start.getBlue();

        int r2 = end.getRed();
        int g2 = end.getGreen();
        int b2 = end.getBlue();

        double rjump = (double) (r2 - r1) / (size - 1);
        double gjump = (double) (g2 - g1) / (size - 1);
        double bjump = (double) (b2 - b1) / (size - 1);

        int r, g, b;
        for (int i = 0; i < size; i++) {
            r = (int) (r1 + i * rjump);
            g = (int) (g1 + i * gjump);
            b = (int) (b1 + i * bjump);
            gradient[i] = new Color(r, g, b);
        }

        return gradient;
    }
}
