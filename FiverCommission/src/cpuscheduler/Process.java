package cpuscheduler;

public class Process {
    private String name;
    private String type;
    private int arrivalTime;
    private int burstTime;
    private int remainingTime;
    private String status;
    private int priority;

    public Process(String name, String type, int arrivalTime, int burstTime) {
        this.name = name;
        this.type = type;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.status = "waiting";
        setInitialPriority();
    }

    private void setInitialPriority() {
        switch (type) {
            case "system":
                priority = 1;
                break;
            case "interactive":
                priority = 2;
                break;
            case "batch":
                priority = 3;
                break;
            default:
                priority = 3;
                break;
        }
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void decrementRemainingTime() {
        remainingTime--;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void demote() {
        if (priority < 3) {
            priority++;
        }
    }

    @Override
    public String toString() {
        return name + " (" + type + ") Arrival: " + arrivalTime + " Burst: " + burstTime + " Remaining: " + remainingTime + " Status: " + status;
    }
}
