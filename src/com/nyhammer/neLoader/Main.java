package com.nyhammer.neLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

/**
 * 
 * @author McFlyboy
 * 
 * @since 0.1.0a
 *
 */
public class Main{
	public static final int VERSION_MAJOR = 0;
	public static final int VERSION_MINOR = 1;
	public static final int VERSION_REVISION = 0;
	public static final int VERSION_PATCH = 0;
	public static final String PRE_VERSION_SUFFIX = "a";
	private Scanner scanner;
	private String chapter;
	private String steamLocation;
	private String gameRunID;
	private String saveLocation;
	public static String getVersion(){
		StringBuilder version = new StringBuilder();
		version.append(String.format("%d.%d.%d", VERSION_MAJOR, VERSION_MINOR, VERSION_REVISION));
		String patch = String.format("%02d", VERSION_PATCH);
		if(!patch.equals("00")){
			version.append("_" + patch);
		}
		version.append(PRE_VERSION_SUFFIX);
		return version.toString();
	}
	private void start(){
		scanner = new Scanner(System.in);
		run();
		stop();
	}
	private void run(){
		System.out.println("NaissanceE-Loader v" + getVersion());
		System.out.println("Created by McFlyboy\n");
		File settings = new File("NE-Loader_settings.cfg");
		if(!settings.exists()){
			return;
		}
		try{
			BufferedReader settingReader = new BufferedReader(new FileReader(settings));
			String line;
			while((line = settingReader.readLine()) != null){
				String[] parser = line.split("=");
				if(parser[0].equals("steam-location")){
					steamLocation = parser[1];
				}
				else if(parser[0].equals("NE-Game-runID")){
					gameRunID = parser[1];
				}
				else if(parser[0].equals("save-location")){
					saveLocation = parser[1];
				}
				else{
					continue;
				}
			}
			settingReader.close();
		}
		catch(FileNotFoundException e1){
			e1.printStackTrace();
			return;
		}
		catch(IOException e){
			e.printStackTrace();
			return;
		}
		System.out.println("Which game-chapter do you want to load? (1 - 7)");
		chapter = scanner.nextLine();
		File chapterSave;
		if(chapter.equals("1")){
			chapterSave = new File("Chapter-saves/1 - Follow The Light/UDKNE_Safeguard.ini");
		}
		else if(chapter.equals("2")){
			chapterSave = new File("Chapter-saves/2 - Going Down/UDKNE_Safeguard.ini");
		}
		else if(chapter.equals("3")){
			chapterSave = new File("Chapter-saves/3 - Breath Compression/UDKNE_Safeguard.ini");
		}
		else if(chapter.equals("4")){
			chapterSave = new File("Chapter-saves/4 - Deeper Into Madness/UDKNE_Safeguard.ini");
		}
		else if(chapter.equals("5")){
			chapterSave = new File("Chapter-saves/5 - Interlude/UDKNE_Safeguard.ini");
		}
		else if(chapter.equals("6")){
			chapterSave = new File("Chapter-saves/6 - Endless Dive/UDKNE_Safeguard.ini");
		}
		else if(chapter.equals("7")){
			chapterSave = new File("Chapter-saves/7 - Meet The Host/UDKNE_Safeguard.ini");
		}
		else{
			return;
		}
		File currentSave = new File(saveLocation);
		if(currentSave.exists()){
			currentSave.delete();
		}
		try{
			Files.copy(chapterSave.toPath(), currentSave.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES, LinkOption.NOFOLLOW_LINKS);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		System.out.println("Do you want to start the game now? (y/n)");
		String string = scanner.nextLine();
		if(string.equals("y")){
			try {
				new ProcessBuilder(steamLocation, gameRunID).start();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	private void stop(){
		scanner.close();
		System.exit(0);
	}
	public static void main(String[] args){
		new Main().start();
	}
}