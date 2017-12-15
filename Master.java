import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Master{
	public static void main(String[] args){
		Scanner input = new Scanner(System.in);
		System.out.println("Enter word: ");
		String word = input.nextLine();
		new WordGuesser(word);
	}
}