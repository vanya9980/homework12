package Exercise1;

class TimeDisplay {
    public static void main(String[] args) {
        // Перший потік: кожну секунду відображає час, що минув від запуску програми
        Thread timeThread = new Thread(() -> {
            int secondsElapsed = 0;
            while (true) {
                try {
                    Thread.sleep(1000);
                    secondsElapsed++;
                    System.out.println("Пройшло секунд: " + secondsElapsed);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        // Другий потік: кожні 5 секунд виводить повідомлення "Минуло 5 секунд"
        Thread messageThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                    System.out.println("Минуло 5 секунд");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        // Запуск обох потоків
        timeThread.start();
        messageThread.start();
    }
}