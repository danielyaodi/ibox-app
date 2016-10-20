
package edu.csupomona.cs585.ibox.sync;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.Delete;
import com.google.api.services.drive.Drive.Files.Insert;
import com.google.api.services.drive.Drive.Files.List;
import com.google.api.services.drive.Drive.Files.Update;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

 

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;


public class GoogleDriveFileSyncManagerTest {

	public Drive mockDrive;
	public GoogleDriveFileSyncManager fileManager;
	public java.io.File localFile;
	public File file;
	public Files mockFiles;
	public Insert mockInsert;
	public Update mockUpdate;
	public List mockRequest;
	public Delete mockDelete;
	public FileList mockFileList;
	public ArrayList<File> list;
	private String fileName = "UnitTest";
	private String fileID = "CS585";
	 
	@Before
	public void setUp() {
		
		mockDrive = mock(Drive.class);
		fileManager = new GoogleDriveFileSyncManager(mockDrive);
		localFile = mock(java.io.File.class);
		mockFiles = mock(Files.class);
		mockInsert = mock(Insert.class);
		mockRequest = mock(List.class);
		mockUpdate = mock(Drive.Files.Update.class);
		mockDelete = mock(Drive.Files.Delete.class);
		mockFileList = new FileList();
		list = new ArrayList<File>();
		 
		 
		file = new File();
		file.setTitle(fileName);
		file.setId(fileID);
		list.add(file);
		mockFileList.setItems(list);
	}

	@Test
	public void testAddFile() throws IOException {
		
		when(mockDrive.files()).thenReturn(mockFiles);
		when(mockFiles.insert(any(File.class), any(FileContent.class))).thenReturn(mockInsert);
		when(mockInsert.execute()).thenReturn(file);
		
		fileManager.addFile(localFile);
		
		verify(mockDrive).files();
		verify(mockFiles).insert(any(File.class) , any(FileContent.class ));
		verify(mockInsert).execute();
			 
	}

	@Test
	public void testUpdateFile() throws IOException{	
		
		 
		when(localFile.getName()).thenReturn(fileName);
		when(mockFiles.list()).thenReturn(mockRequest);
		when(mockRequest.execute()).thenReturn(mockFileList);		 
		when(mockDrive.files()).thenReturn(mockFiles);
		when(mockFiles.update(isA(String.class), isA(File.class), isA(AbstractInputStreamContent.class))).thenReturn(mockUpdate);
		when(mockUpdate.execute()).thenReturn(file);

		fileManager.updateFile(localFile);
		 
		verify(localFile, atLeast(1)).getName();
		verify(mockFiles).list();
		verify(mockRequest).execute();
		verify(mockDrive, atLeast(1)).files();
		verify(mockFiles).update(isA(String.class), isA(File.class), isA(AbstractInputStreamContent.class));
		verify(mockUpdate).execute();
		
	 
	}

	@Test
	public void testDeleteFile() throws IOException {
		
		when(localFile.getName()).thenReturn(fileName);
		when(mockFiles.list()).thenReturn(mockRequest);
		when(mockRequest.execute()).thenReturn(mockFileList);
		when(mockDrive.files()).thenReturn(mockFiles);
		when(mockFiles.delete(fileID)).thenReturn(mockDelete);
		when(mockDelete.execute()).thenReturn(null);
		
		fileManager.deleteFile(localFile);
		
		verify(localFile, atLeast(1)).getName();
		verify(mockFiles).list();
		verify(mockRequest).execute();
		verify(mockDrive, atLeast(1)).files();
		verify(mockFiles).delete(fileID);
		verify(mockDelete).execute();
	 
	}
 
	@Test
	public void testGetFileId() throws IOException{
		 

		when(mockDrive.files()).thenReturn(mockFiles);
		when(mockFiles.list()).thenReturn(mockRequest);
		when(mockRequest.execute()).thenReturn(mockFileList);
		  
		Assert.assertEquals(fileName, file.getTitle());
	 
		 
	}

}
