package cards;

public class Book {

    private String title;
    String author;
    private int count;


    public Book() {

    }

    public Book(String title, String author, int count) {
        this.title = title;
        this.author = author;
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getCount() {
        return count;
    }

}
