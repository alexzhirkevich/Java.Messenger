package com.messenger.protocol;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@Root
public class User implements Serializable {

	private static final long SerialVersionUID = 1L;

	@Attribute(name = "id")
	Integer id;

	@Element(name = "firstName")
	String firstName;

	@Element(name = "lastName")
	String lastName;

	@Element(name = "phone")
	String phone;

	@Element(name = "online")
	Boolean online;

	@Element(name = "picture")
	Byte[] avatar;

	protected User() {
		this.firstName = "unknown";
		this.phone = "unknown";
	}

	public User(@Element(name = "firstName")Integer id,
				@Element(name = "firstName") String firstName,
				@Element(name = "lastName") String lastName,
				@Element(name = "phone") String phone,
				@Element(name = "online") Boolean online,
				@Element(name = "picture") Byte[] picture) {
		setId(id);
		setFirstName(firstName);
		setLastName(lastName);
		setPhone(phone);
		setOnline(online);
		setAvatar(picture);
	}

	public User(User user) {
		this(user.id, user.firstName, user.lastName, user.phone,user.online,user.avatar);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public void setOnline(Boolean online) {
		this.online = online;
	}

	public Boolean isOnline() {
		return online;
	}

	public Byte[] getAvatar() {
		return Arrays.copyOf(avatar, avatar.length);
	}

	public void setAvatar(Byte[] avatar) {
		this.avatar = avatar !=null? Arrays.copyOf(avatar, avatar.length) : null;
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
