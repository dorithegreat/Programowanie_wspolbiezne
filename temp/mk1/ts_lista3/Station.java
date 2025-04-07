import java.util.Random;

public class Station {
    String name;
    int position;
    Cable cable;
    boolean sending = true;
    int messageLength;
    int sentLength = 0;
    int backoffCounter = -1;
    int maxBackoffCounter = 1;
    boolean send = false;

    public Station(String name, int position, Cable cable) {
        this.name = name;
        this.position = position;
        this.cable = cable;
        this.messageLength = 2 * cable.size;
    }

    public void tick() {
        if (sending) {
            if (sentLength < messageLength) {
                if (cable.cable[position].name.equals("")) {
                    cable.cable[position] = new Packet(1, 1, name);
                    sentLength++;
                } else if (sentLength > 0) {
                    sending = false;
                    sentLength = 0;
                    Random rand = new Random();
                    backoffCounter = rand.nextInt((int) Math.pow(2, maxBackoffCounter)) * 2 * cable.size;
                    maxBackoffCounter++;
                }
            } else {
                sending = false;
                send = true;
            }
        } else if (backoffCounter > 0) {
            backoffCounter--;
        } else if (backoffCounter == 0) {
            sending = true;
            backoffCounter--;
        }
    }
}
