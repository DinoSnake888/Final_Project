import java.util.ArrayList;


public class BookManagment {
    ArrayList<Book> books = new ArrayList<Book>();

//    public static void main(String[] args){
//        Book m = new Book("", "", "", 1, false);
//        books.add(m);
//    }
    public BookManagment(){

    }

    //Start of all the book methods
    public void createBook(Book book){

       //books.add(book);
    }

    public void removeBook(Book book){
        books.remove(book);
    }

    public void updateBook(){

    }

    public void sortAlphabetical(){

    }

    public void checkOut(Book book){
        if (!book.checked){
            book.checked = true;
            System.out.println("This book is now checked out");
        }
        else{
            System.out.println("This book has already been checked out");
        }

    }

    public void checkIn(Book book){
        if (book.checked){
            book.checked = false;
            System.out.println("This book is now checked in");
        }
        else{
            System.out.println("This book has already been checked in");
        }

    }

    String accessCode = "meow";


}

