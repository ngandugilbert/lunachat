package bluetooth;

public class Test {
    public static void main(String[] args) {
        var server = new Server();
        var thread = new Thread(server);
        thread.start();
    }
}
