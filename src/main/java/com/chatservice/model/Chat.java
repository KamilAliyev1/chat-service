package com.chatservice.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.Set;



@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chats")
@Entity(name = "chat")
@Builder
@ToString(of = "ID")
@EqualsAndHashCode(of="ID")
public class Chat {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;



    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_chat",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> members;



    @OneToMany(mappedBy = "chat",fetch = FetchType.LAZY)
    private List<Message> messages;
    @Enumerated(EnumType.STRING)
    private ChatType chatType;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType;
    }
}
