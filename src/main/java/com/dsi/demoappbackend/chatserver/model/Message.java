package com.dsi.demoappbackend.chatserver.model;

import com.dsi.demoappbackend.appuser.AppUser;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Message {

    private String senderName;
    private String receiverName;
    private String message;
    private Date date;
    private MsgStatus status;
}
