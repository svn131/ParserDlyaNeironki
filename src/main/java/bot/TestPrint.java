package bot;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class TestPrint {
    public static void main(String[] args) throws FileNotFoundException {

            // Создание объекта PrintStream и связывание его с текстовым документом
            PrintStream out = new PrintStream(new FileOutputStream("C:/logOutput.txt"));

            // Перенаправление стандартного потока вывода в объект PrintStream
            System.setOut(out);

            // Вся последующая информация, выводимая в консоль, будет записана в текстовый документ
            System.out.println("Пример вывода в текстовый документ");

            // Закрытие объекта PrintStream и освобождение ресурсов
            out.close();

    }
}