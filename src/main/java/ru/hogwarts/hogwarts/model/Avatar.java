package ru.hogwarts.hogwarts.model;


import javax.persistence.*;

@Entity(name = "avatar")
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String filePath;

    @Column
    private String mediaType;

    @Column
    private long fileSize;

    @Column
    @Lob
    private byte[] data;

    @OneToOne(cascade = CascadeType.ALL)
    private Student student;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Avatar() {
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "id=" + id +
                ", student=" + student +
                '}';
    }
}
