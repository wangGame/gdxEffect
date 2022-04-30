public class App {
    public static void main(String[] args) {
        System.out.println("---------------------");

        try {
            System.out.println("dddd");
            int i = 1/0;
        }catch (Exception e){
            System.out.println("catch");
        }

        System.out.println("---------------------");
    }
}
