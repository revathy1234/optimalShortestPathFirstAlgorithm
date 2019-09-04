import java.io.*; 
import java.util.Scanner;
import java.util.LinkedList;
import static java.lang.System.exit;
public class find_route{
    
    static LinkedList start=new LinkedList();
    static LinkedList end=new LinkedList();
    static LinkedList distance=new LinkedList();
    static String[] input=new String[3];
    static LinkedList<String> visited=new LinkedList<String>();   
    static LinkedList fringe=new LinkedList();
    static String subdest;
    static String dist;
    static int dis;
    static int closed=0;
     static int d=0;
    
    static int count=0;
    static int curr=0;
    static int dislength;
    static int startlen;
    static int i=0;
    static int endlen;
    
    
                             //reference from https://codereview.stackexchange.com/questions/8521/insert-sort-on-a-linked-list
    public static Node addNode(Node node) {
     if (node == null)
        return null;
    Node orderedLi = node;
    node = node.next;
    orderedLi.next = null;

    while(node != null) {
        final Node current = node;
        node = node.next;
        if (current.cost < orderedLi.cost) {
            current.next = orderedLi;
            orderedLi = current;
        } else {
            Node search = orderedLi;
            while(search.next != null && current.cost > search.next.cost)
                search = search.next;
            current.next = search.next;
            search.next = current;
        }
    }
    return orderedLi;
}
    public static void main (String[] args) throws IOException
 {
    if(args[0].equals("uninf"))
    {
     try {
      
      File file = new File(args[1]);
      FileReader fr = new FileReader(file);
      StringBuffer sr = new StringBuffer();
      BufferedReader br = new BufferedReader(fr);
      Scanner scanner=new Scanner(System.in);
      String line;
                                                //https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
      while ((line = br.readLine()) != null) 
                        {
                              if(line.contains("END OF INPUT"))
                              {
                              }
                              else{
                          
                            input=line.split(" ");
                            start.add(input[0]);
                            end.add(input[1]);
                            distance.add(input[2]);
                            i++;
                            
                          }}
      fr.close();
      
int a,b;
  
       String startn = args[2];
       String endn=args[3];
      
       int d=0;
       int first=0;
      
     Node so=new Node(null,0,0,null);
     int n=start.size();
     a=0;
     for(a=0;a<n;a++)
     {
     
     String name= (String)start.get(a);   
    
         if(name.equals(startn) && !visited.contains(startn))
         {         
             if(first==0)
             {           
             so=new Node(startn,d,0,null);
             first=1;
             fringe.add(count,so);
             count++;
             d=d+1;
             }
             String subdest=(String)end.get(a);
             String dist=(String)distance.get(a);
             int dis=Integer.parseInt(dist);
             Node newn=new Node(subdest,d,dis,so);
             fringe.add(count,newn);
             count++;
              }
        
     }
     d=0;
     for(a=0;a<end.size();a++)
     {
         String name= (String)end.get(a);   
         if(name.equals(startn) && !visited.contains(startn))
         {if(first==0)
             {
             so=new Node(startn,d,0,null);
             first=1;
             fringe.add(so);
             d=d+1;
             }
             String subdest=(String)start.get(a);
             String dist=(String)distance.get(a);
             int dis=Integer.parseInt(dist);
             Node newn=new Node(subdest,d,dis,so);
             fringe.add(newn);
                       
         }
     }
visited.add(startn);
     
                            //Backtracking the fringe to print the optimal route
while(!fringe.isEmpty())
       {
           
        Node least=(Node)fringe.get(0);
        if((least.name).equals(endn))
        {  
           
           System.out.println( "Distance :" +least.obtaincost()+"km");//entire path cost
           closed=1;
           int totalcost=least.obtaincost();
           int prevcost=totalcost;
           Node trace=least.obtainprev();
           System.out.print( "route:\n" + endn+" to " );
           while(trace!=null)
           {
               String name=trace.obtaincurrent();
               int tracecost=trace.obtaincost();
               totalcost=prevcost-tracecost;
               prevcost=tracecost;
               
               System.out.print(name+",");
               System.out.println(totalcost +"km");
               if(name!=startn)
               System.out.print(name+" to ");
               trace=trace.obtainprev();
           }
           exit(0);
        }
        fringe.remove(0);
        d=d+1;
        if(!fringe.isEmpty()){
               Node newstart=(Node)fringe.get(0);
               int cost=newstart.obtaincost();
               String newstate=newstart.obtaincurrent();
               int newfrn=0;
              if(!visited.contains(newstate)) 
              {
             for(a=0;a<start.size();a++)
           {
            String saveme=(String)start.get(a);
               if(saveme.equals(newstate))
                {
                    String des=(String)end.get(a);
                    String dist=(String)distance.get(a);
                    int dist1=Integer.parseInt(dist);
                    dist1=dist1+(int)cost;
                     
                     Node newnode1=new Node(des,d,dist1,newstart);
                     
                     for(int y=0;y<fringe.size();y++)
                     {
                         Node node1=(Node)fringe.get(y);
                         String cityval=node1.obtaincurrent();
                         double costval=node1.obtaincost();
                         if(cityval.equals(des))
                         {
                             newfrn=1;
                          }
                     
                     }

                     if(!visited.contains(des)&&(newfrn==0))
                     fringe.add(newnode1);
                     else
                     {
                      for(int c=0;c<fringe.size();c++)
                      {
                         Node node2=(Node)fringe.get(c);
                         String cityvalue=node2.obtaincurrent();
                         double costvalue=node2.obtaincost();
                         if(cityvalue.equals(des))
                         {
                             if(dist1<costvalue)
                             {
                                 fringe.remove(node2);
                                 fringe.add(newnode1);
                             }
                         }
                         
                      }
                     }
                }
                }


         newfrn=0;

           for(a=0;a<start.size();a++)
            {
              String newcity=(String)end.get(a);
              if(newcity.equals(newstate))
             {
                     String des=(String)start.get(a);
                     String dist=(String)distance.get(a);
                     int dist1=Integer.parseInt(dist);
                     dist1=dist1+(int)cost;
                     Node newnode1=new Node(des,d,dist1,newstart);
                       for(int y=0;y<fringe.size();y++)
                     {
                         Node node1=(Node)fringe.get(y);
                         String cityval=node1.obtaincurrent();
                         int costval=node1.obtaincost();
                         if(cityval.equals(des))
                         {
                             newfrn=1;
                          }
                     
                     }
                      if(!visited.contains(des)&&(newfrn==0))
                          fringe.add(newnode1);
                       else
                     {
                      for(int q=0;q<fringe.size();q++)
                      {
                         Node node2=(Node)fringe.get(q);
                         String cityvalue=node2.obtaincurrent();
                         int costvalue=node2.obtaincost();
                         if(cityvalue.equals(des))
                         {
                             if(dist1<costvalue)
                             {
                                 fringe.remove(node2);
                                 fringe.add(newnode1);
                             }
                         }
                         
                      }
                     }
                 }
             }
             
             
         visited.add(newstate);
       }  } 
        
     }                                                                                       //when no path exists
       if(closed==0)
       {
           System.out.println("Distance:"  +"infinity" + "\n"  +  "route   : none");
       }
      } 
 catch (IOException e) 
    {
      e.printStackTrace();
    }
  }
 }
  static class Node
    {
      
       String name;
       int cost;
       int d;
       Node prev;
       Node next;
       Node(String a, int b, int c,Node p){
       this.name=a;
       this.d=b;
       this.cost=c;
       this.prev=p;
       }
        public String obtaincurrent()
       {
           return name;
       }
         public  Node obtainprev()
       {
           return prev;
       }
       public  int obtaincost()
       {
           return cost;
       }
       
       public  int obtaindepth()
       {
           return d;
       }
        
    }
}
