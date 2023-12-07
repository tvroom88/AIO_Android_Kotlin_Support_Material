const http = require("http")
const Socket = require("websocket").server
const server = http.createServer(()=>{})

server.listen(3000,()=>{
    
})

const webSocket = new Socket({httpServer:server})


webSocket.on('request',(req)=>{
    const connection = req.accept()
   
    connection.on('message',(message)=>{
        const data = JSON.parse(message.utf8Data)
        console.log(data);
        connection.send(data)
    })

})
