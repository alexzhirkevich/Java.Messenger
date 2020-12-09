package com.messenger.application.protocol;



import org.simpleframework.xml.Attribute;

import java.io.Serializable;
import java.util.Objects;


public class User implements Serializable {

	private static final long SerialVersionUID = 1L;

	@Attribute
	int id;

	@Attribute
	String firstName;

	@Attribute
	String lastName;

	@Attribute
	String phone;

	protected User() {
		this.firstName = "unknown";
		this.phone = "unknown";
	}

	public User(String firstName, String lastName, String phone) {
		setFirstName(firstName);
		setLastName(lastName);
		setPhone(phone);
	}

	public User(User user) {
		this(user.firstName, user.lastName, user.phone);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return id == user.id &&
				Objects.equals(firstName, user.firstName) &&
				Objects.equals(lastName, user.lastName) &&
				Objects.equals(phone, user.phone);
	}
}
