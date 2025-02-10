import * as THREE from "three";
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader.js';
import * as CANNON from 'cannon-es';
import CannonDebugger from 'cannon-es-debugger';
import GUI from 'lil-gui';


const carUrl = new URL('../assets/robot1.glb', import.meta.url)
const robotUrl = new URL('../assets/robot0.glb', import.meta.url)
const mapUrl = new URL('../assets/uiuMap.glb', import.meta.url);
const assetLoader = new GLTFLoader();

let obstacleCounter = []

console.log("obstacleCounter", obstacleCounter.length);
let robot0Body;
let robot0;
let cubex;
let axesHelper;
let controls;
let keyboard = {};
let enableFollow = true;
let obstacleBody;
let cubeBody, planeBody;
let slipperyMaterial, groundMaterial;
// cannon variables
let world;
let cannonDebugger;
let timeStep = 1 / 60;
let decide_controle_robot_0 = 0;
let decide_controle_robot_1 = 0;
let phx = 1.2;
let phy = 0.5;
let phz = 2.69;


class S_User {
    static name;
    static id;
    robot_bought = [2];
    controle_robot = [2]
    credit = 0;
    I_am_using = 0;

    constructor(id) {
        this.id = id;
    }

}


var Users = [];

Users.push(new S_User(0));
Users.push(new S_User(1));

for (let i = 0; i < Users.length; i++) {
    for (let j = 0; j < Users[i].robot_bought.length; j++) {
        Users[i].robot_bought[j] = 0;
        Users[i].controle_robot[j] = 0;
        Users[i].credit = 50;
    }
}


let ovi_data_id = 1;

Users[ovi_data_id].I_am_using = 1;





function renderScenes() {
    var renderer = new THREE.WebGLRenderer();
    renderer.setSize(window.innerWidth, window.innerHeight);
    renderer.setPixelRatio(window.devicePixelRatio);
    renderer.shadowMap.enabled = true;
    renderer.outputEncoding = THREE.sRGBEncoding;
    document.body.appendChild(renderer.domElement);
    renderer.setClearColor(0xA3A3A3);
    var scene = new THREE.Scene();

    return { renderer, scene };
}

function cameraCreation() {
    var camera = new THREE.PerspectiveCamera(
        75,
        window.innerWidth / window.innerHeight,
        0.1,
        1000
    );
    return camera;
}
function axisControle() {
    axesHelper = new THREE.AxesHelper(100);
    //scene.add( axesHelper );
}

function orbitControle() {
    controls = new OrbitControls(camera, renderer.domElement);
    controls.rotateSpeed = 1.0
    controls.zoomSpeed = 1.2
    controls.enablePan = false
    controls.dampingFactor = 0.2
    controls.minDistance = 10
    controls.maxDistance = 500
    controls.enabled = true

    //controls.update();
}

var { renderer, scene } = renderScenes();
var camera = cameraCreation();
camera.position.z = 10;
camera.position.y = 5;
const ambientLight = new THREE.AmbientLight(0x404040, 2); // Color and intensity
scene.add(ambientLight);

const directionalLight = new THREE.DirectionalLight(0xffffff, 1.5); // Color and intensity
directionalLight.position.set(10, 20, 10); // Position the light
directionalLight.castShadow = true; // Enable shadows if needed
scene.add(directionalLight);
const lightHelper = new THREE.DirectionalLightHelper(directionalLight);
scene.add(lightHelper);


axisControle();
orbitControle();
let cube = addBox();

console.log("HIIIIIIIIIIIIII", cube);
addKeyListener();
let camerView = 1;
initCannon();
addPlane();
addCubeBodyPhysics();
addGUI();
let obstacle = addObstacle();
addPlaneBody();
addContactMaterials();
addObstacleBody();
let obstacleCubex = addObstacleCubex();
let obstacleRobot0 = addObstacleRobot0();
cubexLoader();
robot0Loader();
robot0BodyPhysics();
addMapLoader();
addMapBodyUIU()

animate();

function cubexLoader() {
    assetLoader.load(carUrl.href, function (gltf) {
        cubex = gltf.scene.children[0];
        //cubex.rotateX +=1;
        scene.add(cubex);

    }, undefined, function (error) {
        console.error(error);
    });
}

function robot0Loader() {
    assetLoader.load(robotUrl.href, function (gltf) {
        robot0 = gltf.scene.children[0];
        robot0.quaternion.x += Math.PI / 2;
        scene.add(robot0);

    }, undefined, function (error) {
        console.error(error);
    });
}

function addMapLoader() {
    assetLoader.load(mapUrl.href, function (gltf) {
        const map = gltf.scene;

        map.position.y -= 8.8;
        map.position.z -=100
        scene.add(map);


    }, undefined, function (error) {
        console.error(error);
    }
    );
}

function addMapBodyUIU() {
    let cubeShape = new CANNON.Box(new CANNON.Vec3(phx + 43.5, phy + 10, phz + 50));
    slipperyMaterial = new CANNON.Material('slippery');
    let cubeBody = new CANNON.Body({ mass: 1000 });
    cubeBody.addShape(cubeShape);
    cubeBody.quaternion.y += 1.22
    cubeBody.position.set(42, 10, 175-100);
    world.addBody(cubeBody);
}

function addKeyListener() {
    window.addEventListener('keydown', function (event) {

        keyboard[event.keyCode] = true;

    }, false);

    window.addEventListener('keyup', function (event) {

        keyboard[event.keyCode] = false;

    }, false);

}
let flag = 0;
function animate() {

    if (cubex) {
        cubex.position.x = cubeBody.position.x+0.1;
        cubex.position.y = cubeBody.position.y +1.2;
        cubex.position.z = cubeBody.position.z-0.4;

        cubex.quaternion.copy(cubeBody.quaternion);

        obstacleCubex.position.copy(cubeBody.position);
        obstacleCubex.quaternion.copy(cubeBody.quaternion);
    }

    if (robot0) {

        robot0.position.x = robot0Body.position.x;
        robot0.position.y = robot0Body.position.y + 0.55;
        robot0.position.z = robot0Body.position.z;



        robot0.quaternion.copy(robot0Body.quaternion);

        obstacleRobot0.quaternion.copy(robot0Body.quaternion);
        obstacleRobot0.position.copy(robot0Body.position);


    }

    obstacle.position.copy(obstacleBody.position);
    obstacle.quaternion.copy(obstacleBody.quaternion);
    renderer.render(scene, camera);

    if(Users[ovi_data_id].controle_robot[1])
    {
        movePlayerCar();
    }
    if (Users[ovi_data_id].controle_robot[0]) {
        movePlayerRobot0();
    }


    if (enableFollow) {
        if (camerView % 2 == 1) followPlayerInThirdPerson();
        if (camerView % 2 == 0) followPlayerInFirstdPerson();
    }

    world.step(timeStep);
    cannonDebugger.update();



    requestAnimationFrame(animate);
}


function addBox() {
    let xParameters = 1;
    let yParameters = 1;
    let zParameters = 1;
    let geometry = new THREE.BoxGeometry(phx, phy, phz);
    let material = new THREE.MeshBasicMaterial({ color: 'pink' });
    let cube = new THREE.Mesh(geometry, material);
    //cube.position.y = yParameters/2;
    //scene.add(cube);

    return cube;
}



function get_Controle() {
    if (keyboard[97] || keyboard[49]) {
        decide_controle_robot_0 = 1;
        decide_controle_robot_1 = 0;
    }
    if (keyboard[98] || keyboard[50]) {
        decide_controle_robot_0 = 0;
        decide_controle_robot_1 = 1;
    }
}


function movePlayerRobot0() {

    const strengthWS = 30;
    const forceForward = new CANNON.Vec3(0, 0, strengthWS);
    const forceBack = new CANNON.Vec3(0, 0, -strengthWS);
    if (keyboard[87]) {
        console.log("W");
        //cube.translateZ(-0.1);
        robot0Body.applyLocalForce(forceForward);
    }
    // down letter S
    if (keyboard[83]) {
        //cube.translateZ(+0.1);
        robot0Body.applyLocalForce(forceBack);
    }

    const strengthAD = 60;
    const forceLeft = new CANNON.Vec3(0, +strengthAD, 0);
    const forceRigth = new CANNON.Vec3(0, -strengthAD, 0);
    // left letter A
    if (keyboard[65]) {
        //cube.rotateY(+0.01);
        robot0Body.applyTorque(forceLeft);
    }

    // up letter D
    if (keyboard[68]) {
        //cube.rotateY(-0.01);
        robot0Body.applyTorque(forceRigth);

    }

}

function movePlayerCar() {
    // up letter W
    const strengthWS = 30;
    const forceForward = new CANNON.Vec3(0, 0, strengthWS);
    const forceBack = new CANNON.Vec3(0, 0, -strengthWS);
    if (keyboard[87]) {
        console.log("W");
        //cube.translateZ(-0.1);
        cubeBody.applyLocalForce(forceForward);
    }
    // down letter S
    if (keyboard[83]) {
        //cube.translateZ(+0.1);
        cubeBody.applyLocalForce(forceBack);
    }

    const strengthAD = 60;
    const forceLeft = new CANNON.Vec3(0, +strengthAD, 0);
    const forceRigth = new CANNON.Vec3(0, -strengthAD, 0);
    // left letter A
    if (keyboard[65]) {
        //cube.rotateY(+0.01);
        cubeBody.applyTorque(forceLeft);
    }

    // up letter D
    if (keyboard[68]) {
        //cube.rotateY(-0.01);
        cubeBody.applyTorque(forceRigth);

    }

    //Change cameraView V
    if (keyboard[86]) {
        if (camerView == 1) {
            camerView = 2;
            setTimeout(() => {
                console.log("After 1 seconds delay");

                console.log(camerView);
            }, 1000);
        }
        else if (camerView == 2) {
            camerView = 1;
            setTimeout(() => {
                console.log("After 1 seconds delay");

                console.log(camerView);
            }, 1000);
        }



    }
}
var yPos = 1;
function followPlayerInThirdPerson() {
    camera.position.x = cubeBody.position.x;
    camera.position.y = cubeBody.position.y + yPos + 4;
    camera.position.z = cubeBody.position.z + 5;
    //camera.position.copy(cube.position);
}

function followPlayerInFirstdPerson() {
    camera.position.x = cubeBody.position.x;
    camera.position.y = cubeBody.position.y + yPos + 4;
    camera.position.z = cubeBody.position.z - 4;
}

function addPlane() {
    let geometry = new THREE.BoxGeometry(1000, 0, 1000);
    let material = new THREE.MeshBasicMaterial({ color: 'gray' });
    let planeThree = new THREE.Mesh(geometry, material);
    planeThree.position.set(0, 0, -90);
    scene.add(planeThree);
}

function addObstacle() {
    let geometry = new THREE.BoxGeometry(1, 1, 1);
    let col = 'green'
    let material = new THREE.MeshStandardMaterial({ color: col });
    let obstacle = new THREE.Mesh(geometry, material);
    obstacle.position.set(0, 0.5, -10);
    scene.add(obstacle);

    return obstacle;
}

function addObstacleCubex() {
    let geometry = new THREE.BoxGeometry(0.5, 0.5, 0.5);
    let col = 'green'
    let material = new THREE.MeshStandardMaterial({ color: col });
    let obstacle = new THREE.Mesh(geometry, material);
    obstacle.position.set(0, 0.5, -10);
    scene.add(obstacle);

    return obstacle;
}

function addObstacleRobot0() {
    let geometry = new THREE.BoxGeometry(0.5, 0.5, 0.5);
    let col = 'green'
    let material = new THREE.MeshStandardMaterial({ color: col });
    let obstacle = new THREE.Mesh(geometry, material);
    obstacle.position.set(0, 0.5, -10);
    scene.add(obstacle);

    return obstacle;
}



obstacle.col = "green";

function initCannon() {
    world = new CANNON.World();
    world.gravity.set(0, -9.8, 0);

    initCannonDebugger();

}

function initCannonDebugger() {
    cannonDebugger = new CannonDebugger(scene, world, {
        onInit(body, mesh) {
            document.addEventListener("keydown", (event) => {
                if (event.key === "f") {
                    mesh.visible = !mesh.visible;
                }
            });
        },
    });
}



function addCubeBodyPhysics() {
    let cubeShape = new CANNON.Box(new CANNON.Vec3(phx+0.65, phy, phz+0.08));
    slipperyMaterial = new CANNON.Material('slippery');
    cubeBody = new CANNON.Body({ mass: 10, material: slipperyMaterial });
    cubeBody.addShape(cubeShape);
    cubeBody.quaternion.y += (Math.PI) * 112;
    cubeBody.position.set(0, 2, 0);
    world.addBody(cubeBody);

}

function robot0BodyPhysics() {

    let cubeShape = new CANNON.Box(new CANNON.Vec3(phx + 1, phy, phz));
    robot0Body = new CANNON.Body({ mass: 10, material: slipperyMaterial });
    robot0Body.addShape(cubeShape);
    robot0Body.quaternion.y += (Math.PI) * 112;
    robot0Body.position.set(10, 2, 0);
    world.addBody(robot0Body);

}


function addGUI() {
    gui = new GUI();
    const options = {
        orbitsControls: true
    }

    gui.add(options, 'orbitsControls').onChange(value => {
        if (value) {
            controls.enabled = true;
            enableFollow = false;
        } else {
            controls.enabled = false;
            enableFollow = true;
        }
    });
}

function addPlaneBody() {
    groundMaterial = new CANNON.Material('ground')
    const planeShape = new CANNON.Box(new CANNON.Vec3(500, 0.01, 500));
    planeBody = new CANNON.Body({ mass: 0, material: groundMaterial });
    planeBody.addShape(planeShape);
    planeBody.position.set(0, 0, -90);
    world.addBody(planeBody);
}

function addObstacleBody() {
    let obstacleShape = new CANNON.Box(new CANNON.Vec3(0.5, 0.5, 0.5));
    obstacleBody = new CANNON.Body({ mass: 0.001 });
    obstacleBody.addShape(obstacleShape);
    obstacleBody.position.set(0, 0.5, -10);
    world.addBody(obstacleBody);
    console.log(obstacleBody, "cubeBody");

}

function addContactMaterials() {
    const slippery_ground = new CANNON.ContactMaterial(groundMaterial, slipperyMaterial,
        {
            friction: 0.0009,
            restitution: 0.1,
            contactEquationStiffness: 1e8,
            contactEquationRelaxation: 5,
        }
    );
    world.addContactMaterial(slippery_ground);
}




obstacleCounter.push(obstacleRobot0);
obstacleCounter.push(obstacleCubex);





let storeI = 0;

console.log("StoreIIIII", storeI);


for (var i = 0; i < obstacleCounter.length; i++) {
    let obstacle = obstacleCounter[i];

    // Create a custom context menu
    const contextMenu = document.createElement('div');
    contextMenu.style.position = 'absolute';
    contextMenu.style.display = 'none';
    contextMenu.style.backgroundColor = '#2c3e50';
    contextMenu.style.color = '#ecf0f1';
    contextMenu.style.padding = '15px';
    contextMenu.style.borderRadius = '8px';
    contextMenu.style.boxShadow = '0 4px 10px rgba(0, 0, 0, 0.5)';
    contextMenu.style.transition = 'all 0.2s ease';
    contextMenu.style.fontFamily = 'Arial, sans-serif';
    document.body.appendChild(contextMenu);

    // Add menu items with styling
    contextMenu.innerHTML = `
    <div class="menu-item" data-obstacle-index="${i}">Buy</div>
    <div class="menu-item" data-obstacle-index="${i}">Take Control</div>
    `;

    // Add obstacle name to the top of the menu bar
    const obstacleName = document.createElement('div');
    obstacleName.textContent = `Robot ${i + 1}`;
    obstacleName.style.fontWeight = 'bold';
    obstacleName.style.marginBottom = '10px';
    contextMenu.prepend(obstacleName);

    // Apply styles to the menu items
    document.querySelectorAll('.menu-item').forEach(item => {
        item.style.padding = '10px 20px';
        item.style.cursor = 'pointer';
        item.style.borderRadius = '4px';
        item.style.marginBottom = '5px';
        item.style.transition = 'background-color 0.2s ease, color 0.2s ease';

        item.addEventListener('mouseover', () => {
            item.style.backgroundColor = '#3498db';
            item.style.color = '#fff';
        });
        item.addEventListener('mouseout', () => {
            item.style.backgroundColor = '';
            item.style.color = '';
        });
    });

    document.querySelectorAll('.menu-item').forEach(item => {
        item.addEventListener('click', (event) => {
            const obstacleIndex = event.target.getAttribute('data-obstacle-index');
            const menuText = event.target.textContent;

            if (menuText === "Buy") {
                for (let j = 0; j < Users.length; j++) {
                    if (Users[ovi_data_id] != Users[j].id) {
                        checkBuy = Users[j].robot_bought[obstacleIndex]
                    }
                }
                if (!checkBuy && Users[ovi_data_id].credit >= 50) {
                    Users[ovi_data_id].robot_bought[obstacleIndex] = 1;
                    Users[ovi_data_id].credit -=50;
                    alert('Congratulations!!! You Bought This');
                }
                else if (checkBuy && Users[ovi_data_id].robot_bought[obstacleIndex] != 1) {

                    alert('Sorry. Someone Has bought this already');
                    if(Users[ovi_data_id].credit<50)
                    {
                        alert('Also You do not have enough credit!!!, So, Later if some one Leave it then your credit should be minimum 50 to buy this, You can buy credit for your profiles credit option');
                    }
                }
                else if (Users[ovi_data_id].robot_bought[obstacleIndex] == 1) {

                    alert('You have bought this already');
                    if(Users[ovi_data_id].credit<50)
                    {
                        alert('Also You do not have enough credit!!!, So, Later if you want to buy any other robot then your credit should be minimum 50 to buy that, You can buy credit for your profiles credit option');
                    }
                }
                else if(Users[ovi_data_id].credit<50)
                {
                    alert('You do not have enough credit!!! Your credit should be minimum 50 to buy this, You can buy credit for your profiles credit option');
                }
            }
            else if (menuText === "Take Control") {
                if (Users[ovi_data_id].robot_bought[obstacleIndex] == 1) {
                    Users[ovi_data_id].controle_robot[obstacleIndex] = 1
                    for(let k = 0; k<Users[ovi_data_id].controle_robot.length; k++)
                    {
                        if(k != obstacleIndex)
                        {
                            Users[ovi_data_id].controle_robot[k] = 0;
                        }
                    }
                    alert('You are in control now');
                }
                else {
                    alert('Sorry some one else have bought this or you didnt buy this, You cant control this');
                }
            }

            storeI = obstacleIndex;
            contextMenu.style.display = 'none';
        });
    });

    const raycaster = new THREE.Raycaster();
    const mouse = new THREE.Vector2();

    const glowGeometry = new THREE.SphereGeometry(3, 32, 32);
    const glowMaterial = new THREE.MeshBasicMaterial({
        color: 0xffff00,
        emissive: 0xffff00,
        emissiveIntensity: 100,
        transparent: true,
        opacity: 0.5
    });
    const glowMesh = new THREE.Mesh(glowGeometry, glowMaterial);
    glowMesh.visible = false;
    scene.add(glowMesh);

    window.addEventListener('contextmenu', (event) => {
        event.preventDefault();

        mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
        mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;

        raycaster.setFromCamera(mouse, camera);
        const intersects = raycaster.intersectObject(obstacle);

        if (intersects.length > 0) {
            contextMenu.style.left = `${event.clientX}px`;
            contextMenu.style.top = `${event.clientY}px`;
            contextMenu.style.display = 'block';

            obstacle.material.emissive.setHex(0xffff00);
            glowMesh.position.copy(obstacle.position);
            glowMesh.visible = true;
        } else {
            contextMenu.style.display = 'none';
        }
    });

    window.addEventListener('click', () => {
        contextMenu.style.display = 'none';
        glowMesh.visible = false;
    });

    window.addEventListener('mousemove', (event) => {
        mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
        mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;

        raycaster.setFromCamera(mouse, camera);
        const intersects = raycaster.intersectObject(obstacle);

        if (intersects.length > 0) {
            glowMesh.position.copy(obstacle.position);
            glowMesh.visible = true;
        } else {
            glowMesh.visible = false;
        }
    });
}


