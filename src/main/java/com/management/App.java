package com.management;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.java.Address;
import com.java.FactoryProvider;
import com.java.NewUser;

public class App {
	public static void main(String[] args) {
		System.out.println("User Management Project");
		Session session = FactoryProvider.getFactory().openSession();
		Boolean count = true;

		System.out.println("Press 1 For Login");
		System.out.println("Press 2 For SignUP");
		Scanner scan = new Scanner(System.in);
		int usertype = scan.nextInt();
		Boolean logincheck = true;
		Query query;
		Query query1;
//			while (count) {
		if (usertype == 1) {
			System.out.println("Enter the User Id");
			String userid = scan.next();
			System.out.println("Enter the User Password");
			scan.nextLine();
			String password = scan.nextLine();

//	        	to be continue
			query = session.createQuery("from NewUser where userId = '" + userid + "'");
			NewUser user5 = (NewUser)session.get(NewUser.class, userid);
			System.out.println(user5);
			List<NewUser> list = query.list();
			if (list.size() >= 1) {
				String username = "";
				String pass = "";
				for (NewUser user : list) {
					username = user.getUsername();
					pass = user.getPassword();
				}
				if (!username.equals(userid) || !password.equals(pass)) {
					System.out.println("please Enter the valid username or password");
//							continue;
				} else {
					System.out.println("press 1 for show all user list");
					System.out.println("press 2 for search user");
					System.out.println("press 3 for delete user");
					System.out.println("press 4 for update user");
					String operation = scan.nextLine();
//							scan.nextLine();
					if (operation.equals("1")) {
						query = session.createQuery("from NewUser");
						NewUser user1 = session.get(NewUser.class, 1l);
//								query1 = session.createQuery("from Address");
						List<NewUser> userlist = query.list();
//								List<Address> addresslist = query1.list();
						for (NewUser user : userlist) {
							System.out.print(user.getFirstname());
							System.out.print("\t" + user.getLastname());
							for (Address useraddress : user.getAddress()) {
								System.out.print("\t" + useraddress.getCountry());
								System.out.print("\t" + useraddress.getState());
								System.out.print("\t" + useraddress.getCity());
								System.out.print("\t" + useraddress.getPincode());
								System.out.println("\t" + useraddress.getRoad());
								System.out.print("\t");
							}
						}
					} else if (operation.equals("2")) {
						System.out.println("Enter name or anything else for search");
						String searchelement = scan.nextLine();
						Query searchquery = session.createQuery(
								"From NewUser as u where u.firstname like ?1 or u.lastname like ?1 or u.userid like ?1");
						searchquery.setString(1, "%" + searchelement + "%");
						System.out.println(searchelement);
						List<NewUser> searchlist = searchquery.list();
						for (NewUser user : searchlist) {
							System.out.print(user.getFirstname());
							System.out.print("\t" + user.getLastname());
							for (Address useraddress : user.getAddress()) {
								System.out.print("\t" + useraddress.getCountry());
								System.out.print("\t" + useraddress.getState());
								System.out.print("\t" + useraddress.getCity());
								System.out.print("\t" + useraddress.getPincode());
								System.out.println("\t" + useraddress.getRoad());
							}
							System.out.print("\n\n\n");
						}
					} else if (operation.equals("3")) {
						System.out.println("Enter the User Id for delete");
						String deletedid = scan.next();
						query = session.createQuery("from NewUser where userId = '" + deletedid + "'");
						List<NewUser> deleteduser = query.list();
						Transaction transaction = session.beginTransaction();
						session.delete(deleteduser.get(0));
						transaction.commit();
						System.out.println("User deleted Successfully");
					} else if (operation.equals("4")) {
						System.out.println("Enter the User Id for Update");
						String updateid = scan.next();
						scan.nextLine();
						query = session.createQuery("from NewUser where userId = '" + updateid + "'");
						List<NewUser> updatelist = query.list();
						Transaction transaction = session.beginTransaction();
						NewUser user = updatelist.get(0);
						System.out.println("Enter the name for update");
						String updatename = scan.nextLine();
						user.setFirstname(updatename);
						transaction.commit();
					} else {

					}
				}

			}
		} else if (usertype == 2) {
			System.out.println("Enter Your First Name");
			scan.nextLine();
			String firstname = scan.nextLine();
			System.out.println("Enter Your Last Name");
			String lastname = scan.nextLine();
			Boolean emailexist = true;
			String userid = "";
			System.out.println("Enter Your UserId");
			userid = scan.nextLine();
			query = session.createQuery("from NewUser where userId = '" + userid + "'");
			while (emailexist) {
				if (query.list().size() >= 1) {
					System.out.println("This UserId already exist\n\nPlease Enter again");
					userid = scan.nextLine();
					query = session.createQuery("from NewUser where userId = '" + userid + "'");
				} else {
					emailexist = false;
				}
			}
			System.out.println("Enter Your Password");
			String password = scan.nextLine();
			Boolean addcount = true;
			int i = 1;
			NewUser newuser = new NewUser();
			newuser.setFirstname(firstname);
			newuser.setLastname(lastname);
			newuser.setUsername(userid);
			newuser.setPassword(password);
			session.save(newuser);
			List<Address> addresslist = new ArrayList();
			while (addcount) {
				Address address = new Address();
				System.out.println("Enter Your " + i + " Address");
				System.out.println("Enter Your Country");
				String country = scan.nextLine();
				System.out.println("Enter Your State");
				String state = scan.nextLine();
				System.out.println("Enter Your City");
				String city = scan.nextLine();
				System.out.println("Enter Your Pin Code");
				String pincode = scan.nextLine();
				System.out.println("Enter Your Road");
				String road = scan.nextLine();
				address.setCountry(country);
				address.setCity(city);
				address.setState(state);
				address.setPincode(pincode);
				address.setRoad(road);
				addresslist.add(address);
				address.setUser(newuser);
				newuser.setAddress(addresslist);
				session.save(address);
				System.out.println("Do you want to add more Address");
				System.out.println("1. NO   2. YES");
				String addmore = scan.nextLine();
				if (addmore.equals("1") || addmore.equalsIgnoreCase("no")) {
					addcount = false;
				}
			}
		} else {
			System.out.println("You Pressed wrong key");
//				break;
		}

//		}
		session.close();
	}
}
