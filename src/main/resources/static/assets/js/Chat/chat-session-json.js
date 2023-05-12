function sendMsgAlarm(){
    let uuid = document.querySelector("#ws-romeUuid").value;
    const jsonObj = {
        "resultCode": "SUCCESS",
        "result": {
            "type":"NEWCHAT",
            "roomInfo":{
                "uuid": uuid
            }
        }
    }
    return jsonObj;
} //채팅 보냈을때 알려주기..

function sendDelRoomAlarm(uuid, msg, id){
    const jsonObj = {
        "resultCode": "SUCCESS",
        "result": {
            "type":"DELETEROOM",
            "roomInfo":{
                "uuid": uuid,
                "id" : id
            },
            "msg": msg
        }
    }
    return jsonObj;
}

function sendDelChatAlarm(id){
    const jsonObj = {
        "resultCode": "SUCCESS",
        "result": {
            "type":"DELETEMSG",
            "chatId": id
        }
    }
    return jsonObj;
}
