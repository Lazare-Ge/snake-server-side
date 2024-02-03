const RSocketWebSocketClient = require("rsocket-websocket-client").default;
const RSocketClient = require('rsocket-core').RSocketClient;
const MESSAGE_RSOCKET_ROUTING = require('rsocket-core').MESSAGE_RSOCKET_ROUTING;


const defaultHelloRequest = {
    number: 1,
    string: 'hello',
};

const dimension = 30;

const argv = {
    host: 'localhost',
    port: 7000,
    stream_route: 'hello.world.request.stream',
    stream_default_payload: JSON.stringify(defaultHelloRequest),
    fire_input_route: 'hello.world.fire.forget',
};

Promise.resolve(run(argv)).then(
    () => process.exit(0),
    error => {
        console.error(error.stack);
        process.exit(1);
    },
);

// run(argv).then(r => console.log("THEN!"));

async function run(options) {
    drawBoard(
        {
            initialFoodPixel: {x:5, y:5},
            initialSnakePixels: [{x:10, y:10}]
        }
    );

    const socket = await connect(options);
    initInputFiring(socket)
    return getRequestStreamPromise(socket, options);
}


function drawBoard(gameStartInfo){
    let table = document.createElement('table');
    let foodPixel = gameStartInfo.initialFoodPixel;
    let snakePixels = gameStartInfo.initialSnakePixels;
    table.className = 'snake-game-table';

    for (let i=0; i< dimension; i++) {
        let row = table.insertRow();
        for (let j = 0; j < dimension; j++) {
            let cell = row.insertCell();
            cell.id = getCellId(i,j);

        }
    }

    document.body.appendChild(table);

    // updateBoard(snakePixels, foodPixel);
}


function initInputFiring(socket){
    document.addEventListener('keydown', (event) => {
        const direction = getDirectionFromKey(event.key);
        if (direction) {
            const payload = {
                direction: direction
            };
            socket.fireAndForget({
                data: JSON.stringify(payload),
                metadata: encodeRoutingMetadata('change.direction').toString('utf-8')
            });
            console.log(`Sent: ${direction}`);
        }
    });
}

let subscription;

function getRequestStreamPromise(socket, options){
    let pending = 1000000;
    return new Promise((resolve, reject) => {
        socket.requestStream({
            // data: options.stream_default_payload,
            metadata: encodeRoutingMetadata("stream.game").toString('utf-8'),
        }).subscribe({
            onComplete() {
                resolve();
                console.log('onComplete()');
            },
            onError(error) {
                console.log('onError(%s)', error.message);
                reject(error);
            },
            onNext(payload) {
                // console.log('onNext(%s)', payload.data);
                const stateUpdate = JSON.parse(payload.data);
                const snakePixels = stateUpdate.snakePixels;
                const foodPixel = stateUpdate.foodPixel;
                updateBoard(snakePixels, foodPixel)
                if (--pending === 0) {
                    console.log('cancel()');
                    subscription.cancel();
                    resolve();
                }
            },
            onSubscribe(_subscription) {
                console.log('requestStream(%s)', pending);
                subscription = _subscription;
                subscription.request(pending);
            },
        });
    });
}


async function connect(options) {
    const client = new RSocketClient({
        setup: {
            dataMimeType: 'application/json',
            keepAlive: 1000000, // avoid sending during test
            lifetime: 100000,
            metadataMimeType: MESSAGE_RSOCKET_ROUTING.string,

        },
        transport: new RSocketWebSocketClient({
            debug: true,
            url: 'ws://localhost:7000',
            // Uses global WebSocket constructor supported by browser.
            wsCreator: url => new WebSocket(url)
        }),
    });
    return await client.connect();
}


function encodeRoutingMetadata(route) {
    const length = Buffer.byteLength(route)
    const buffer = Buffer.alloc(1)
    buffer.writeInt8(length)
    return Buffer.concat([buffer, Buffer.from(route, 'utf-8')])
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

function getCellId(x, y){
    return `cell-${x}-${y}`
}



function updateBoard(snakePixels, foodPixel){
    for (let i=0; i<dimension; i++){
        for(let j=0; j<dimension; j++) {
            let cell = document.getElementById(getCellId(i, j))
            cell.className = 'empty';
        }
    }

    for (let i=0; i<snakePixels.length; i++){
        let x = snakePixels[i].t1;
        let y = snakePixels[i].t2;
        let cell = document.getElementById(getCellId(x,y))
        cell.className = 'snake';
    }

    let foodCell = document.getElementById(getCellId(foodPixel.t1, foodPixel.t2));
    foodCell.className = 'food';
}





















