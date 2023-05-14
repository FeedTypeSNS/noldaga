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

function sendDelRoomAlarm(uuid, msg, id, data){
    const jsonObj = {
        "resultCode": "SUCCESS",
        "result": {
            "type":"DELETEROOM",
            "roomInfo":{
                "uuid": uuid,
                "id" : id
            },
            "msg": msg,
            "data": data
        }
    }
    return jsonObj;
}

function sendDelChatAlarm(id, uuid, chatId, msg){
    const jsonObj = {
        "resultCode": "SUCCESS",
        "result": {
            "type":"DELETEMSG",
            "roomInfo":{
                "uuid": uuid,
                "id" : id,
                "chatId": chatId
            },
            "msg": msg
        }
    }
    return jsonObj;
}
