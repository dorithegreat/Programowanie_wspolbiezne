import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Simulation {
    Cable cable;
    List<Station> stations = new ArrayList<>();

    public Simulation(int size) {
        this.cable = new Cable(size);
    }

    public void addStation(String name, int position) {
        stations.add(new Station(name, position, cable));
    }

    public void run() {
        int step = 1;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("simulation.txt"))) {
            while (true) {
                System.out.println("Step " + step + ":");
                display(writer);

                cable.propagate();

                for (Station station : stations) {
                    station.tick();
                }

                if (end()) {
                    break;
                }

                Thread.sleep(100);
                step++;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean end() {
        for (Station station : stations) {
            if (!station.send) {
                return false;
            }
        }
        return true;
    }

    private void display(BufferedWriter writer) throws IOException {
        char[] stationPositions = new char[cable.size];
        Arrays.fill(stationPositions, ' ');

        for (Station station : stations) {
            stationPositions[station.position] = station.name.charAt(0);
        }

        System.out.println("Cable:   " + cable.toString());
        System.out.println("Stations:" + new String(stationPositions));

        writer.write(cable.toString());
        writer.newLine();
    }
}
