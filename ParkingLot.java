package myparkingprj;

import java.util.concurrent.TimeUnit;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.*;
import myparkingprj.ParkingLot.Car;
import java.awt.*;

public class ParkingLot {
    int SIZE = 0;
    float price =10;
    Scanner sc = new Scanner(System.in);
    JFrame f=new JFrame();
    public class Car {
        private String regNo;
        private String color;
        private String tag;		//Normal,EV,Disabled
        private Date entryTime;
        private String name;
        public Car(String regNo, String color, String name, String tag) {
            this.regNo = regNo;
            this.color = color;
            this.name = name;
            this.entryTime = new Date();
            this.tag = tag;
            this.name = name;
        }
        
        public String getRegNo() {
        	return regNo;
        }
        public String getName() {
        	return name;
        }
        public String getColor() {
        	return color;
        }
        public String getTag() {
        	return tag;
        }
        public Date getEntryTime() {
        	return entryTime;
        }
    }
    										
    ArrayList<Integer> availableSlotList;       // Available slots list
    Map<Integer, Car> map1;      // Map of Slot, Car
    Map<String, Integer> map2;     // Map of RegNo, Slot
    Map<Integer, String> map3;     // Map of slot, tag

    public void createParkingLot(int lotCount, ArrayList<Integer> evList, ArrayList<Integer> disabledList) {
        try {
            this.SIZE = lotCount;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(f,"Invalid lot count", "Alert", JOptionPane.ERROR_MESSAGE);
        }
        this.availableSlotList = new ArrayList<Integer>() {};
        for (int i=1; i<= this.SIZE; i++) {
            availableSlotList.add(i);
        }
        
        this.map1 = new HashMap<Integer, Car>();
        this.map2 = new HashMap<String, Integer>();
        this.map3 = new HashMap<Integer, String>();
        for (int i=1; i <= this.SIZE; i++) {
        	if(evList.contains(i))
        		this.map3.put(i,"EV");
        	else if(disabledList.contains(i))
        		this.map3.put(i,"Disabled");
        	else
        		this.map3.put(i,"Normal");
        }
        JOptionPane.showMessageDialog(f,"Created parking lot with " + lotCount + " slots", "Created", JOptionPane.PLAIN_MESSAGE);
    }
    
    public void park(String regNo, String color, String name, String tag) {
    	int slot=0;
        if (this.SIZE == 0) {
            JOptionPane.showMessageDialog(f,"Sorry, parking lot is not created", "Alert", JOptionPane.ERROR_MESSAGE);
        } else if (this.map1.size() == this.SIZE) {
            JOptionPane.showMessageDialog(f,"Sorry, parking lot is full", "Alert", JOptionPane.ERROR_MESSAGE);
        } else {
            Car car = new Car(regNo, color, name, tag);
            Collections.sort(availableSlotList);
            int i=0; 
            while(i<this.SIZE) {
            	
            	if(car.getTag().equals(this.map3.get(availableSlotList.get(i)))) {
            		slot = availableSlotList.get(i);
            		break;
            	}
            	else if(i==(availableSlotList.size()-1)) {
                    JOptionPane.showMessageDialog(f,"No slots available for "+car.getTag()+" tag.", "Alert", JOptionPane.ERROR_MESSAGE);
                    break;
            	}
            	i++;
            }
            if(slot!=0) {
            	this.map1.put(slot, car);
            	this.map2.put(regNo, slot);
                JOptionPane.showMessageDialog(f,"Allocated slot number: " + slot, "Alloted", JOptionPane.PLAIN_MESSAGE);
            	System.out.println("Allocated slot number: " + slot);
            	System.out.println();
            	availableSlotList.remove(i);
            }
            else
            	System.out.println();
            
        }
    }
    public void leave(int slotNo) {
        if (this.SIZE == 0) {
            JOptionPane.showMessageDialog(f,"Sorry, parking lot is not created", "Alert", JOptionPane.ERROR_MESSAGE);
        } else if (this.map1.size() > 0) {
            Car carToLeave = this.map1.get(slotNo);
            if (carToLeave != null) {
                this.map1.remove(slotNo);
                this.map2.remove(carToLeave.getRegNo());
                this.availableSlotList.add(slotNo);
                System.out.println("Slot number " + slotNo + " is free");
                Date exittime = new Date();
                long diff = exittime.getTime() - carToLeave.getEntryTime().getTime();
                long durationMin = TimeUnit.MILLISECONDS.toMinutes(diff);
                float charge = durationMin * price;
                JOptionPane.showMessageDialog(f,"Slot number " + slotNo + " is free\nPrice to pay: "+ charge, "AMOUNT", JOptionPane.PLAIN_MESSAGE);
                
            } else {
                JOptionPane.showMessageDialog(f,"Slot number " + slotNo + " is already empty", "Alert", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(f,"Parking lot is empty", "Alert", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void getSlotNumberFromRegNo(String regNo) {
        if (this.SIZE == 0) {
            JOptionPane.showMessageDialog(f,"Sorry, parking lot is not created", "Alert", JOptionPane.ERROR_MESSAGE);
        } else if (this.map2.containsKey(regNo)) {
            JOptionPane.showMessageDialog(f,"Car with registration number " +regNo+ " is at slot number "+this.map2.get(regNo), "Location", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(f,"Not found", "Alert", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void status(int func,String regnoKey1) {
        if (this.SIZE == 0) {
            JOptionPane.showMessageDialog(f,"Sorry, parking lot is not created", "Alert", JOptionPane.ERROR_MESSAGE);
            System.out.println("Sorry, parking lot is not created");
            System.out.println();
        } else {
            // Print the current status.
            JFrame frame = new JFrame("Parking Lot");
            frame.setSize(550, 850);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            final RectangleComponent component = new RectangleComponent(func,regnoKey1);
            frame.setVisible(true);
            JPanel panel=new JPanel();
            panel.add(component);
            frame.add(panel);    
            
            System.out.println("Slot No.\tRegistration No.\tColor");
            Car car;
            for (int i = 1; i <= this.SIZE; i++) {
                if (this.map1.containsKey(i)) {
                    car = this.map1.get(i);
                    System.out.println(i + "\t\t" + car.getRegNo() + "\t\t" + car.getColor() + "\t\t" + car.getTag() +"\t\t" + car.getEntryTime());
                }
            }
            System.out.println();
        } 
    }
    
    public void changePrice(int pricef) {
        price=pricef;
    	JOptionPane.showMessageDialog(f,"New price is set to "+ price+ " Rs per minute.", "Price Changed", JOptionPane.PLAIN_MESSAGE);
    }
    
    public class RectangleComponent extends JPanel {
        public ArrayList <Rectangle> boxes;
        int func1;
        int brdMin;
        Car car;
        String regnoKey;
        private int width = 65;
        private int height = 80;
        private int startX = 20;
        private int startY = 40;

        public RectangleComponent(int function, String regnoKey1) {
            func1=function;
            regnoKey=regnoKey1;
            boxes = new ArrayList<Rectangle>();
            int k=0;
            for (int i = 0; i <= SIZE/6 ;i++){
                if(k<SIZE){
                    for(int j=0; j < 6; j++)
                    {
                        if(i%2==0)
                            brdMin=startY-2;
                        else
                            brdMin=0;
                        if(k<SIZE){
                            boxes.add(new Rectangle(startX+(width+2)*j, (startY+(height+startY)*i)-brdMin, width, height));
                            k++;
                        }
                        else
                            break;
                    }    
                }
            }
        }
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            for (int i = 0; i < boxes.size(); i++){
                double strX=boxes.get(i).getX();
                double strY=boxes.get(i).getY();
                g2.draw(boxes.get(i));
                if(func1==0 && !availableSlotList.contains(i+1)) {                          //0-status,1-location
                    car = map1.get(i+1);
                    g2.setColor(Color.RED);
                    g2.fill(boxes.get(i));
                    g2.setColor(Color.BLACK);
                    g.setFont(new Font("TimesRoman", Font.PLAIN, 10)); 
                    g2.drawString(car.getRegNo(),(int)strX+1,(int)strY+65);
                    g2.drawString(car.getName(),(int)strX+1,(int)strY+75);
                }
                else if(func1==1 && (i+1==map2.get(regnoKey))){
                    g2.setColor(Color.BLUE);
                    g2.fill(boxes.get(i));
                }
                else{
                    g2.setColor(Color.GREEN);
                    g2.fill(boxes.get(i));
                }
                g2.setColor(Color.BLACK);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 19)); 
                g2.drawString(String.valueOf(i+1),(int)strX+25,(int)strY+20);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 24));
                if("EV".equals(map3.get(i+1)))
                    g2.drawString("EV",(int)strX+18,(int)strY+55);
                else if("Disabled".equals(map3.get(i+1)))
                    g2.drawString("PH",(int)strX+18,(int)strY+55);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(7*(width+2)+startX, 6*(height+startY)+startY);
        }

    }   
}    