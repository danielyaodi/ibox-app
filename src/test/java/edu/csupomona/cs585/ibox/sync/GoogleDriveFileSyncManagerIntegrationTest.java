package edu.csupomona.cs585.ibox.sync;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.*;

import com.google.api.services.drive.Drive;

import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManager;
import edu.csupomona.cs585.ibox.sync.GoogleDriveServiceProvider;


public class GoogleDriveFileSyncManagerIntegrationTest {

	private Drive googledrive;
	private GoogleDriveFileSyncManager fileManager;
	private File testFile;
	private String testFileName, dir;
	

	@Before
	public void  setUp() throws Exception{
		googledrive = GoogleDriveServiceProvider.get().getGoogleDriveClient();
		fileManager = new GoogleDriveFileSyncManager(googledrive);
		dir = "c:/doc/test.txt";
		newFile();
		testFileName = testFile.getName();
	}	

	
	public void newFile() throws Exception {
		testFile = new File(dir);
		
		FileWriter fileMaker = new FileWriter(testFile);
		fileMaker.write("Input some data.");
		fileMaker.close();
	}
	public void fileContentChange() throws Exception {
		String changedContent = "change some data.";
		BufferedWriter fileChanger = new BufferedWriter(new FileWriter(dir, true));
		fileChanger.write(changedContent);
		fileChanger.flush();
		fileChanger.close();
	}
	
	 
	@Test
	public void testAddFile() throws Exception {		
		try {
			fileManager.addFile(testFile);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
		System.out.println("testFileName is" + testFileName + "   " + "FileID is" + fileManager.getFileId(testFileName));
	}

	@Test
	public void testUpdateFile() {		
		String fileID = fileManager.getFileId(testFileName);
		assertNotNull(fileID);
				
		try {
			fileContentChange();
			fileManager.updateFile(testFile);
		} catch (Exception e) {			
			System.out.println(e.getMessage());
		}
		System.out.println("testFileName" + testFileName + "   " +  "FileID is" + fileManager.getFileId(testFileName));
	}

	@Test
	public void testDelete() {
		
		try {
			fileManager.deleteFile(testFile);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		String name = fileManager.getFileId(testFileName);
		assertNull(name);
	    System.out.println("The testFile is deleted.");		
	}

	
 
	 
	
}
 