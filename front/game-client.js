const RSocketWebSocketClient = require("rsocket-websocket-client").default;
const RSocketClient = require('rsocket-core').RSocketClient;
const MESSAGE_RSOCKET_ROUTING = require('rsocket-core').MESSAGE_RSOCKET_ROUTING;



Promise.resolve(run()).then(
    // () => process.exit(0),
    // error => {
    //     console.error(error.stack);
    //     process.exit(1);
    // },
);

let initialGameInfo;
async function run() {
    const socket = await connect();
    const gameStartPromise = getGameStartPromise(socket);
    gameStartPromise.then(() => {
        console.log(initialGameInfo);
        initGame(initialGameInfo)
        getRequestStreamPromise(socket, initialGameInfo.gameId).then(() => {
            console.log("STREAMING COMPLETED!");
        });
    });
}

async function connect() {
    const client = new RSocketClient({
        setup: {
            dataMimeType: 'application/json',
            keepAlive: 10000000,
            lifetime: 10000000,
            metadataMimeType: MESSAGE_RSOCKET_ROUTING.string,

        },
        transport: new RSocketWebSocketClient({
            debug: true,
            url: 'ws://localhost:7000',
            wsCreator: url => new WebSocket(url)
        }),
    });
    return await client.connect();
}

function getGameStartPromise(socket) {
    return new Promise((resolve, reject) => {
        socket.requestResponse({
            metadata: encodeRoutingMetadata("game.start").toString('utf-8'),
        }).subscribe({
            onComplete: gameInfo => {
                initialGameInfo = JSON.parse(gameInfo.data);
                console.log(initialGameInfo);
                resolve();
            },
            onError: error => {
                console.error('Connection error:', error);
                reject(error);
            },
        });
    });
}

// Connect to the RSocket server
// client.connect().subscribe({
//     onComplete: socket => {
//         socket.requestResponse({
//             metadata: encodeRoutingMetadata("game.start").toString('utf-8'),
//         }).subscribe({
//             onComplete: gameInfo => {
//                 const initialGameInfo = JSON.parse(gameInfo.data);
//                 initGame(initialGameInfo);
//                 // Start streaming
//                 const gameId = initialGameInfo.gameId;
//                 console.log(gameId);
//                 let subscription = null;
//             },
//             onError: error => {
//                 console.error('Connection error:', error);
//             },
//         });
//     },
//     onError: error => {
//         console.error('Failed to connect:', error);
//     },
// })

function getRequestStreamPromise(socket, gameId) {
    return new Promise( (resolve, reject) => {
        socket.requestStream({
            // data: JSON.stringify({ gameId: gameId }),
            data: gameId,
            metadata: encodeRoutingMetadata("game.stream").toString('utf-8'),
        }).subscribe({
            onComplete() {
                console.log("Stream completed!");
                document.removeEventListener('keydown');
                resolve();
            },
            onError(error) {
                document.removeEventListener('keydown');
                console.error('Connection error:', error);
                reject(error)
            },
            onSubscribe(_subscription) {
                console.log("Requesting game stream!");
                subscription = _subscription;
                _subscription.request(1000000000);
                initInputFiring(socket, gameId);
            },
            onNext(payload) {
                const updateData = JSON.parse(payload.data);
                updateBoard(updateData.snake, updateData.food, 30)
            }
        });
    })
}

function initGame(initialGameInfo) {
    drawBoard(initialGameInfo);
    const snake = initialGameInfo.snake;
    const food = initialGameInfo.food
    const dimension = initialGameInfo.dimension;
    updateBoard([snake], food, dimension);
    console.log('Game started:', initialGameInfo);
}

function drawBoard(initialGameInfo) {
    let dimension = initialGameInfo.dimension;
    console.log("DIMENSTION: " + dimension);
    let table = document.createElement('table');
    table.className = 'snake-game-table';

    let upperFrame = table.insertRow()
    for(let i=0; i< dimension+2; i++){
        let frameCell = upperFrame.insertCell()
        frameCell.className = 'frame';
    }
    for (let i = 0; i < dimension; i++) {
        let row = table.insertRow();

        let frameCell1 = row.insertCell();
        frameCell1.className = 'frame';

        for (let j = 0; j < dimension; j++) {
            let cell = row.insertCell();
            cell.id = getCellId(i, j);
        }

        let frameCell2 = row.insertCell();
        frameCell2.className = 'frame';
    }
    let lowerFrame = table.insertRow()
    for(let i=0; i< dimension+2; i++){
        let frameCell = lowerFrame.insertCell()
        frameCell.className = 'frame';
    }

    document.body.appendChild(table);
}


function encodeRoutingMetadata(route) {
    const length = Buffer.byteLength(route)
    const buffer = Buffer.alloc(1)
    buffer.writeInt8(length)
    return Buffer.concat([buffer, Buffer.from(route, 'utf-8')])
}

function initInputFiring(socket, gameId){
    document.addEventListener('keydown', (event) => {
        const direction = getDirectionFromKey(event.key);
        if (direction) {
            const payload = {
                direction: direction,
                gameId: gameId
            };
            socket.fireAndForget({
                data: JSON.stringify(payload),
                metadata: encodeRoutingMetadata('game.input').toString('utf-8')
            });
            console.log(`Sent: ${direction}`);
        }
    });
}

function getDirectionFromKey(key) {
    switch (key) {
        case "ArrowUp":
            return "up";
        case "ArrowDown":
            return "down";
        case "ArrowLeft":
            return "left";
        case "ArrowRight":
            return "right";
        default:
            return null;
    }
}

function getCellId(x, y) {
    return `cell-${x}-${y}`
}

function updateBoard(snakePixels, foodPixel, dimension) {
    for (let i = 0; i < dimension; i++) {
        for (let j = 0; j < dimension; j++) {
            let cell = document.getElementById(getCellId(i, j))
            cell.className = 'empty';
        }
    }
    for (let i = 0; i < snakePixels.length; i++) {
        let x = snakePixels[i].x;
        let y = snakePixels[i].y;
        let cell = document.getElementById(getCellId(x, y))
        cell.className = 'snake';
    }
    let foodCell = document.getElementById(getCellId(foodPixel.x, foodPixel.y));
    foodCell.className = 'food';
}



