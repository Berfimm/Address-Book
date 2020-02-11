package application;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Objects;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	int personInArray=0;
	int index=0; //number of elements in array, only changes when person added into array.
	Person[] abArray;
	// Specify the size of five string fields in the record
	final static int ID_SIZE = 4;
	final static int NAME_SIZE = 32;
	final static int STREET_SIZE = 32;
	final static int CITY_SIZE = 20;
	final static int GENDER_SIZE = 1;
	final static int ZIP_SIZE = 5;
	final static int RECORD_SIZE =(ID_SIZE+NAME_SIZE + STREET_SIZE + CITY_SIZE + GENDER_SIZE + ZIP_SIZE);
	public RandomAccessFile raf;


	// Text fields
	TextField tfID = new TextField();
	TextField tfSearchUpdateID = new TextField();
	TextField tfName = new TextField();
	TextField tfStreet = new TextField();
	TextField tfCity = new TextField();
	TextField tfGender = new TextField();
	TextField tfZip = new TextField();


	// Buttons
	Button btAdd = new Button("Add");
	Button btFirst = new Button("First");
	Button btNext = new Button("Next");
	Button btPrevious = new Button("Previous");
	Button btLast = new Button("Last");
	Button btSearchByID = new Button("SearchByID");
	Button btUpdateByID = new Button("UpdateByID");
	Button btClear = new Button("Clean TextFields");


	// Labels
	Label lbID= new Label("ID");
	Label lbSearchUpdateID= new Label("Search/UpdateID");
	Label lbName= new Label("Name");
	Label lbStreet= new Label("Street");
	Label lbCity= new Label("City");
	Label lbGender= new Label("Gender");
	Label lbZip= new Label ("Zip");

	public Main() {

		// Open or create a random access file
		try {
			raf = new RandomAccessFile("address.dat", "rw");
			abArray=new Person[100];

		}
		catch(IOException ex) {
			ex.printStackTrace();
			System.exit(1);
		}



	}



	@Override
	public void start(Stage primaryStage) {
		try {

			tfID.setPrefColumnCount(4);
			//tfID.setDisable(true);
			tfGender.setPrefColumnCount(1);
			tfZip.setPrefColumnCount(4);
			tfCity.setPrefColumnCount(12);

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("Look, an Information Dialog");

			// Pane p1 for holding labels Name, Street, and City
			GridPane p1 = new GridPane();
			p1.setAlignment(Pos.CENTER);
			p1.setHgap(5);
			p1.setVgap(5);
		
			p1.add(lbName, 0, 1);
			p1.add(tfName, 1, 1);

			p1.add(lbStreet, 0, 2);
			p1.add(tfStreet, 1, 2);

			p1.add(lbCity, 0, 3);
			
			p1.add(lbID, 0, 0);
			
			HBox p4 = new HBox(5);
			p4.getChildren().addAll(tfID,lbSearchUpdateID,tfSearchUpdateID);
			p1.add(p4, 1,0);
			

			//Hbox Pane p2 for grid[3][1]

			HBox p2 = new HBox(5);
			p2.getChildren().addAll(tfCity,lbGender,tfGender,lbZip,tfZip);
			p1.add(p2, 1,3);

			//For buttons
			HBox p3 = new HBox(5);
			p3.getChildren().addAll(btAdd,btFirst,btNext,btPrevious,btLast,btUpdateByID,btSearchByID,btClear);
			p3.setAlignment(Pos.CENTER);

			//Border Pane

			BorderPane borderPane= new BorderPane();
			borderPane.setCenter(p1);
			borderPane.setBottom(p3);

			Scene scene = new Scene(borderPane,550,180);
			primaryStage.setTitle("Address Book Project");
			primaryStage.setScene(scene);
			primaryStage.show();

			// Display the first record if exists
			try {

				if (raf.length() > 0) //dosya boþ deðilse, array i doldur.
				{
					long currentPos= raf.getFilePointer();
					while(currentPos < raf.length())
					{
						readFileFillArray(abArray, currentPos);
						currentPos=raf.getFilePointer();
					}
					readFileByPos(0);
				}
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}




			btAdd.setOnAction(e->{

				try {
					
					/*alert.setContentText("YOU PRESSED ADD BUTTON");
					alert.showAndWait();*/

					writeAddressToFile(raf.length());
					readFileFillArray(abArray, RECORD_SIZE*2*(index)); //ADD LAST RECORD(IN THE FILE) TO ARRAY
					alert.setContentText("Record is added successfully");
					alert.showAndWait();
					cleanTextFields();


					
				
			} 
				catch (Exception ex) 
				{
					

					
				}



			});
			

			btFirst.setOnAction(e -> {

				/*if(index>0)
				{
					traverseArray(abArray, index);
				}*/
				index = 0;
				traverseArray(abArray, index);
				
				

			});
		
			
			btLast.setOnAction(e -> {
				index = personInArray -1;
				traverseArray(abArray, index);
				
			});
			
			btNext.setOnAction(e->{
				
				if(index==personInArray-1) {
					alert.setContentText("You already see last person!!!");
					alert.showAndWait();
					
				}
				else {
					index++;
					traverseArray(abArray, index);
				}
				
				
			});
			
			btPrevious.setOnAction(e->{
				

			
				
				if(index==0) {
					alert.setContentText("You already see first person!!!");
					alert.showAndWait();
					
				}
				else {
					index--;
					traverseArray(abArray, index);
				}
				
			
				
				
			});
			
			btSearchByID.setOnAction(e->{
				boolean personFound = false;
				for(int i = 0; i<personInArray; i++) {
					
					if(abArray[i].getId() == Integer.parseInt(tfSearchUpdateID.getText())) {
						
						 personFound=true;
						index = i;
						traverseArray(abArray, index);
					}
				}
				if(personFound==false) {

					alert.setContentText("Not Found");
					alert.showAndWait();
				}
				
			});
			
			btUpdateByID.setOnAction(e->{
				try {
					for(int x = 0; x<personInArray; x++) {
						
						if(abArray[x].getId() == Integer.parseInt(tfSearchUpdateID.getText())) {
							 
							abArray[x].setName(tfName.getText());
							abArray[x].setCity(tfCity.getText());
							abArray[x].setStreet(tfStreet.getText());
							abArray[x].setGender(tfGender.getText());
							abArray[x].setZip(tfZip.getText());
							
							
							raf.seek(RECORD_SIZE*2*(x));
							FileOperation.writeFixedLengthString(tfSearchUpdateID.getText(),ID_SIZE, raf);
							FileOperation.writeFixedLengthString(tfName.getText(), NAME_SIZE, raf);
							FileOperation.writeFixedLengthString(tfStreet.getText(), STREET_SIZE, raf);
							FileOperation.writeFixedLengthString(tfCity.getText(), CITY_SIZE, raf);
							FileOperation.writeFixedLengthString(tfGender.getText(), GENDER_SIZE, raf);
							FileOperation.writeFixedLengthString(tfZip.getText(), ZIP_SIZE, raf);
							
							alert.setContentText("Update is successful !!");
							alert.showAndWait();
							
						}
					}
				}
				catch(Exception ex) {
					
				}
				
				
			});
			
			btClear.setOnAction(e -> {
				try {
					cleanTextFields();
				}
				catch (Exception ex) {

				}
			}); 
	

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/** Write a record at the end of the file */
	public void writeAddressToFile(long position) {
		try {
			int personInArraycc = personInArray + 1;
			raf.seek(position);
			FileOperation.writeFixedLengthString(tfID.getText(),ID_SIZE, raf);
			FileOperation.writeFixedLengthString(tfName.getText(), NAME_SIZE, raf);
			FileOperation.writeFixedLengthString(tfStreet.getText(), STREET_SIZE, raf);
			FileOperation.writeFixedLengthString(tfCity.getText(), CITY_SIZE, raf);
			FileOperation.writeFixedLengthString(tfGender.getText(), GENDER_SIZE, raf);
			FileOperation.writeFixedLengthString(tfZip.getText(), ZIP_SIZE, raf);
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	public void readFileFillArray(Person[]people,long position) throws IOException {
		raf.seek(position);
		String id = FileOperation.readFixedLengthString(ID_SIZE, raf);
		int intID= Integer.parseInt(id.trim().toString());
		String name = FileOperation.readFixedLengthString(NAME_SIZE, raf).trim();
		String street = FileOperation.readFixedLengthString(STREET_SIZE, raf).trim();
		String city = FileOperation.readFixedLengthString(CITY_SIZE, raf).trim();
		String gender= FileOperation.readFixedLengthString(GENDER_SIZE, raf).trim();
		String zip = FileOperation.readFixedLengthString(ZIP_SIZE, raf).trim();


		Person p= new Person(intID, name, gender, street, city, zip);
		people[personInArray]=p;
		personInArray++;
	}

	/** Read a record at the specified position */
	public void readFileByPos(long position) throws IOException {
		raf.seek(position);
		String id = FileOperation.readFixedLengthString(ID_SIZE, raf);
		String name = FileOperation.readFixedLengthString(NAME_SIZE, raf);
		String street = FileOperation.readFixedLengthString(STREET_SIZE, raf);
		String city = FileOperation.readFixedLengthString(CITY_SIZE, raf);
		String gender = FileOperation.readFixedLengthString(GENDER_SIZE, raf);
		String zip = FileOperation.readFixedLengthString(ZIP_SIZE, raf);

		tfID.setText(id);
		tfName.setText(name);
		tfStreet.setText(street);
		tfCity.setText(city);
		tfGender.setText(gender);
		tfZip.setText(zip);
	}
	public void cleanTextFields()
	{
		tfID.clear();
		tfName.clear();
		tfStreet.clear();
		tfCity.clear();
		tfGender.clear();
		tfSearchUpdateID.clear();
		tfZip.clear();

	}
	public void traverseArray(Person[]people,int personInArray)
	{

		tfID.setText(String.valueOf(abArray[personInArray].getId()));
		tfName.setText(abArray[personInArray].getName());
		tfStreet.setText(abArray[personInArray].getStreet());
		tfCity.setText(abArray[personInArray].getCity());
		tfGender.setText(abArray[personInArray].getGender());
		tfZip.setText(abArray[personInArray].getZip());

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
