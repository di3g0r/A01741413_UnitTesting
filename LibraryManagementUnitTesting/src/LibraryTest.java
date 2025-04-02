import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;  
  
public class LibraryTest {  
  
    @Test  
    public void testAddBook() {  
        Library library = new Library();  
        Book book = new Book("1984", "George Orwell");  
        library.addBook(book);  
        assertTrue(library.listAvailableBooks().contains(book));  
    }  
  
    @Test  
    public void testCalculateFineAfterReturn() {  
        // Setup  
        Library library = new Library();  
        Patron patron = new Patron("Alice Smith");  
        Book book = new Book("Design Patterns", "Erich Gamma");  
  
        library.addBook(book);  
        library.addPatron(patron);  
          
        // Check out for 2 days  
        library.checkOutBook(patron, book, 2);   
          
        // Simulate that 2 days have passed, and set the due date  
        book.setDueDate(LocalDate.now().minusDays(2));  
  
        // Return the book  
        library.returnBook(patron);   
  
        // Act: Calculate fine after returning  
        double fineAfterReturn = library.calculateFine(patron);   
  
        // Assert: No fine should be calculated after return  
        assertEquals(0, fineAfterReturn, "The fine should be zero after returning the book.");  
    }  

    //LIBRARY TESTS

    @Test
    public void testAddBookWithError() {
        // Setup - Add the first instance of the book
        Book mobyDick = new Book("Moby Dick", "Herman Melville");
        library.addBook(mobyDick);
        
        // Try to add a duplicate book
        Book duplicateMobyDick = new Book("Moby Dick", "Herman Melville");
        library.addBook(duplicateMobyDick);
        
        // Count the number of books with the title "Moby Dick"
        int mobyDickCount = 0;
        for (Book book : library.listAvailableBooks()) {
            if (book.getTitle().equals("Moby Dick")) {
                mobyDickCount++;
            }
        }
        
        assertEquals(2, mobyDickCount, "Duplicate books");
    }
    
    @Test
    public void testCheckOutNonExistentBook() {
        // Setup
        Patron patron = new Patron("John");
        library.addPatron(patron);
        
        // Create a book but don't add it to the library
        Book nonExistentBook = new Book("Java 101", "Unknown Author");
    
        boolean result = library.checkOutBook(patron, nonExistentBook, 14);
        assertFalse(result, "Checkout should fail for non-existent book");
    }
    
    @Test
    public void testFineCalculation() {
        // Setup
        Patron john = new Patron("John");
        Book book = new Book("Programming in Java", "John Doe");
        
        library.addPatron(john);
        library.addBook(book);
        
        // Check out the book
        library.checkOutBook(john, book, 14);
        
        // Simulate that the book is 2 days overdue
        book.setDueDate(LocalDate.now().minusDays(2));
        
        // Calculate the fine
        double fine = library.calculateFine(john);
        
        // Verify the fine 
        assertEquals(1.0, fine, 0.001, "Fine should be $1.00 for 2 days overdue");
    }
    
    @Test
    public void testListAllBooks() {
        // Setup - Add multiple books
        Book book1 = new Book("1984", "George Orwell");
        Book book2 = new Book("To Kill a Mockingbird", "Harper Lee");
        Book book3 = new Book("The Great Gatsby", "F. Scott Fitzgerald");
        
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        
        // Get the list of all available books
        List<Book> availableBooks = library.listAvailableBooks();
        
        // Verify all books are in the list
        assertEquals(3, availableBooks.size(), "There should be 3 available books");
        assertTrue(availableBooks.contains(book1), "Book1 should be in the list");
        assertTrue(availableBooks.contains(book2), "Book2 should be in the list");
        assertTrue(availableBooks.contains(book3), "Book3 should be in the list");
    }
    
    @Test
    public void testListAllPatrons() {
        // Setup - Add multiple patrons
        Patron patron1 = new Patron("Alice");
        Patron patron2 = new Patron("Bob");
        Patron patron3 = new Patron("Charlie");
        
        library.addPatron(patron1);
        library.addPatron(patron2);
        library.addPatron(patron3);
        
        // Get the list of all patrons
        List<Patron> patrons = library.listPatrons();
        
        // Verify all patrons are in the list
        assertEquals(3, patrons.size(), "There should be 3 patrons");
        assertTrue(patrons.contains(patron1), "Patron1 should be in the list");
        assertTrue(patrons.contains(patron2), "Patron2 should be in the list");
        assertTrue(patrons.contains(patron3), "Patron3 should be in the list");
    }

    //BOOK TESTS

    @Test
    public void testBookCreated() {
        // Create a book with valid information
        String title = "Brave New World";
        String author = "Aldous Huxley";
        
        Book book = new Book(title, author);
        
        // Verify the book was created correctly
        assertEquals(title, book.getTitle(), "Book title should match");
        assertEquals(author, book.getAuthor(), "Book author should match");
        assertFalse(book.isCheckedOut(), "New book should not be checked out");
        assertNull(book.getDueDate(), "New book should not have a due date");
    }
    
    @Test
    public void testBookCheckOutAndReturn() {
        // Create a book
        Book book = new Book("Lord of the Rings", "J.R.R. Tolkien");
        
        // Initially, book should not be checked out
        assertFalse(book.isCheckedOut(), "Book should not be checked out initially");
        assertNull(book.getDueDate(), "Due date should be null initially");
        
        // Check out the book
        book.checkOut(14);
        
        // Verify book status after checkout
        assertTrue(book.isCheckedOut(), "Book should be checked out");
        assertNotNull(book.getDueDate(), "Due date should be set");
        // Due date should be 14 days from today
        LocalDate expectedDueDate = LocalDate.now().plusDays(14);
        assertEquals(expectedDueDate, book.getDueDate(), "Due date should be 14 days from today");
        
        // Return the book
        book.returnBook();
        
        // Verify book status after return
        assertFalse(book.isCheckedOut(), "Book should not be checked out after return");
        assertNull(book.getDueDate(), "Due date should be null after return");
    }
    
    @Test
    public void testSetDueDateException() {
        // Create a book
        Book book = new Book("Pride and Prejudice", "Jane Austen");
        
        // Attempt to set a due date for a book that is not checked out
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            book.setDueDate(LocalDate.now().plusDays(7));
        }, "Setting due date on a book that is not checked out should throw error");
        
        // Verify the exception message
        assertEquals("Cannot set due date for a book that is not checked out.", exception.getMessage());
        
        // Now check out the book
        book.checkOut(10);
        
        // Setting due date should now work without exception
        LocalDate newDueDate = LocalDate.now().plusDays(5);
        book.setDueDate(newDueDate);
        assertEquals(newDueDate, book.getDueDate(), "Due date should be updated to new value");
    }

    //PATRON TESTS

    @Test
    public void testRegisterPatron() {
        // Create a patron with valid information
        String name = "John Doe";
        
        Patron patron = new Patron(name);
        
        // Verify the patron was created with the correct properties
        assertEquals(name, patron.getName(), "Patron name should match");
        assertNotNull(patron.getCheckedOutBooks(), "Checked out books list should be initialized");
        assertEquals(0, patron.getCheckedOutBooks().size(), "New patron should have no checked out books");
    }
    
    @Test
    public void testPatronHasCheckedOutBooks() {
        // Setup
        Patron john = new Patron("John");
        Book mobyDick = new Book("Moby Dick", "Herman Melville");
        
        // Initially, patron should have no books
        assertFalse(john.hasCheckedOutBook(mobyDick), "Patron should not have any books initially");
        
        // Check out a book
        john.checkOutBook(mobyDick);
        
        // Verify patron has the book
        assertTrue(john.hasCheckedOutBook(mobyDick), "Patron should have the book after checkout");
        
        // Get the checked out books
        List<Book> checkedOutBooks = john.getCheckedOutBooks();
        
        // Verify the list contains the book
        assertEquals(1, checkedOutBooks.size(), "Patron should have 1 book");
        assertTrue(checkedOutBooks.contains(mobyDick), "Checked out books should contain Moby Dick");
    }
    
    @Test
    public void testPatronMultipleBooks() {
        // Setup
        Patron patron = new Patron("Alice");
        Book book1 = new Book("The Great Gatsby", "F. Scott Fitzgerald");
        Book book2 = new Book("1984", "George Orwell");
        
        // Initially, patron should have no books
        assertEquals(0, patron.getCheckedOutBooks().size(), "Patron should start with no books");
        
        // Check out multiple books
        patron.checkOutBook(book1);
        patron.checkOutBook(book2);
        
        // Verify patron has both books
        List<Book> checkedOutBooks = patron.getCheckedOutBooks();
        assertEquals(2, checkedOutBooks.size(), "Patron should have 2 books");
        assertTrue(checkedOutBooks.contains(book1), "Checked out books should contain book1");
        assertTrue(checkedOutBooks.contains(book2), "Checked out books should contain book2");
        
        assertTrue(patron.hasCheckedOutBook(book1), "Patron should have book1");
        assertTrue(patron.hasCheckedOutBook(book2), "Patron should have book2");
        
        // Return one book
        patron.returnBook(book1);
        
        // Verify state after returning one book
        assertEquals(1, patron.getCheckedOutBooks().size(), "Patron should have 1 book after returning book1");
        assertFalse(patron.hasCheckedOutBook(book1), "Patron should not have book1 after returning it");
        assertTrue(patron.hasCheckedOutBook(book2), "Patron should still have book2");
    }
}  
 