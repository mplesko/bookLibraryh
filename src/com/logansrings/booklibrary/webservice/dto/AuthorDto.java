package com.logansrings.booklibrary.webservice.dto;

public class AuthorDto {
		private String firstName;
		private String lastName;

		public AuthorDto() {}
		
		public AuthorDto(String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}

		public String toString() {
			return String.format("[%s] firstName:%s lastName:%s ",
					"AuthorDto", firstName, lastName);
		}

		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getAuthorName() {
			return firstName + " " + lastName;
		}


		public String getLastNameFirstName() {
			return lastName + ", " + firstName;
		}
}
