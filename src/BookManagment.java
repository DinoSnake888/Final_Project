
public class BookManagment {

    public BookManagment(){

    }


//Start of all the book methods
    public void createBook(){
        String bookTitle = title.getText();
        String bookAuthor = author.getText();
        String bookYear = year.getText();

        if (bookTitle.equals("") || bookAuthor.equals("") || bookYear.equals("")) {
            return;
        }

        try {

            int yearAsNumber = Integer.Int(bookYear);

            Book newBook = new Book(bookTitle, bookAuthor, bookNumber);

            books.add(newBook);

            title.setText("");
            author.setText("");
            year.setText("");

        } catch (NumberFormatException error) {
            System.out.println("Year is not a number.");

        }
    }

    public void removeBook(){

    }

    public void updateBook(){
        if (newTitle.equals("") || newAuthor.equals("") || newYear.equals("")) {
            System.out.println("Error: Everything must be completed to update.");
            return;
        }

        Book bookToUpdate = null;
        for (Book book : books) {
            if (book.getTitle().equls(titleToFind)) {
                bookToUpdate = book;
                break;
            }
        }
        if (bookToUpdate !=null) {
            try {
                int yearAsNumber = Integer.Int(newYear);
                bookToUpdate.setTitle(newTitle);
                bookToUpdate.setAuthor(newAuthor);
                bookToUpdate.setYear(yearasNumber);
                System.out.println("Book updated successfully.");
            }  catch (NumberFormatException error) {
                System.out.println("Error: Year must be a number.");
            }
        } else {
            System.out.println("Error: Book with title "" + titleToFind + "" not found.");
        }
    }


    public void sortAlphabetical(){

    }

}
