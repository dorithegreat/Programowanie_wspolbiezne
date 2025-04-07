import java.util.Arrays;

public class Cable {
    int size;
    Packet[] cable;

    public Cable(int size) {
        this.size = size;
        this.cable = new Packet[size];
        for (int i = 0; i < size; i++) {
            this.cable[i] = new Packet(0, 0, "");
        }
    }

    public void propagate() {
        Packet[] newCable = new Packet[size];
        for (int i = 0; i < size; i++) {
            newCable[i] = new Packet(0, 0, "");
        }

        for (int i = 0; i < size; i++) {
            if (!cable[i].name.equals("")) {
                if (cable[i].left == 1 && i > 0) {
                    if (!newCable[i - 1].name.equals("") && !newCable[i - 1].name.equals(cable[i].name)) {
                        newCable[i - 1] = new Packet(1, 1, "!");
                    } else {
                        newCable[i - 1] = new Packet(0, 1, cable[i].name);
                    }
                }
                if (cable[i].right == 1 && i < size - 1) {
                    if (!newCable[i + 1].name.equals("") && !newCable[i + 1].name.equals(cable[i].name)) {
                        newCable[i + 1] = new Packet(1, 1, "!");
                    } else {
                        newCable[i + 1] = new Packet(1, 0, cable[i].name);
                    }
                }
            }
        }

        this.cable = newCable;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Packet packet : cable) {
            if (packet.name.equals("")) {
                sb.append(".");
            } else {
                sb.append(packet.name);
            }
        }
        return sb.toString();
    }
}
