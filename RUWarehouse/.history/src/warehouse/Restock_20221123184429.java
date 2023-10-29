package warehouse;

public class Restock {
    public static void main(String[] args) {
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);

        Warehouse w = new Warehouse();
        int productsToAdd = StdIn.readInt();

        for(int i=0; i<productsToAdd; i++)
        {
            String query = StdIn.readString();
            if(query.equals("add"))
            {
                int day = StdIn.readInt();
                int id = StdIn.readInt();
                String name = StdIn.readString();
                int stock = StdIn.readInt();
                int demand = StdIn.readInt();
                w.addProduct(id, name, stock, day, demand);
            }
            else
            {
                int id = StdIn.readInt();
                int stock = StdIn.readInt();
                w.restockProduct(id, stock);
            }
        }
        StdOut.println(w);
    }
}
