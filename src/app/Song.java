// Eyob Tesfaye and Kishan Zalora

package app;

public class Song {
	
	private String song;
	
	private String artist;
	
	private String album;
	
	private String year;
	
	public Song(String song, String artist, String album, String year){
		
		this.song = song;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
	public String getSong(){
		return song;
	}

	public String getAlbum(){
		return album;
	}

	public String getArtist(){
		return artist;
	}

	public String getYear(){
		return year;
	}

	public void setSong(String song){
		this.song = song;
	}

	public void setArtist(String artist){
		this.artist = artist;
	}

	public void setYear(String year){
		this.year = year;
	}

	public void setAlbum(String album ){
		this.album = album;
	}
	
	public int Comparable(Song song){
		return this.getKey().compareTo(song.getKey());
	}
	
	public String toString() {
		return song + " - " + artist;
		
	}
	
	public String getKey(){
		return (song + "\t" + artist).toUpperCase();
	}
	
	public String saveToFile() {
		return song + "\t" + artist + "\t" + album + "\t" + year + "\n";
	}
}
