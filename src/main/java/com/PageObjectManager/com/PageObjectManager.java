package com.PageObjectManager.com;

import org.openqa.selenium.WebDriver;

import com.PageObjectModel.com.AI_JDCreator_Page;
import com.PageObjectModel.com.Registration_Page;
import com.PageObjectModel.com.SignUpandSignOut_Page;

public class PageObjectManager {

	private WebDriver driver;

	private Registration_Page regpage;

	private SignUpandSignOut_Page signpage;

	private AI_JDCreator_Page aijdpage;

	public PageObjectManager(WebDriver driver) {

		this.driver = driver;
	}

	public Registration_Page getRegpage() {

		return (regpage == null) ? regpage = new Registration_Page(driver) : regpage;
	}

	public SignUpandSignOut_Page getSignpage() {

		return (signpage == null) ? signpage = new SignUpandSignOut_Page(driver) : signpage;
	}

	public AI_JDCreator_Page getAijdpage() {

		return (aijdpage == null) ? aijdpage = new AI_JDCreator_Page(driver) : aijdpage;
	}

}
