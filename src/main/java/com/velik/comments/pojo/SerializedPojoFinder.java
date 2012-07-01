package com.velik.comments.pojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.velik.comments.CommentListId;
import com.velik.comments.Finder;

public class SerializedPojoFinder extends DelegatingFinder {
	private static final long serialVersionUID = 1;
	private static final Logger LOGGER = Logger.getLogger(SerializedPojoFinder.class.getName());

	@Override
	public void persist() {
		try {
			new ObjectOutputStream(new FileOutputStream(getFile())).writeObject(delegate);
		} catch (FileNotFoundException e) {
			LOGGER.log(Level.WARNING, e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
	}

	private File getFile() {
		return new File("persistedPojos.ser");
	}

	@Override
	public void initalize() {
		delegate = new FinderPojo();

		// TODO remove
		delegate.createCommentList(new CommentListId("singleton"));
		delegate.createProfile("andreas");
		delegate.createProfile("pasha");

		try {
			delegate = (Finder) new ObjectInputStream(new FileInputStream(getFile())).readObject();
		} catch (FileNotFoundException e) {
			LOGGER.log(Level.INFO, "Found no " + getFile() + ". Starting with empty data model.", e);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
	}
}
