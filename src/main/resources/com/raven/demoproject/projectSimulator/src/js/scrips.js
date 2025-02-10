import * as THREE from 'three';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js';
import * as dat from 'dat.gui'
import * as CANNON from 'cannon-es';


var world = new CANNON.World({
    gravity: new CANNON.Vec3(0, 0, -9.8)
});

const timeStep = 1/60;

const renderer = new THREE.WebGLRenderer();

// Set the canvas size
renderer.setSize(window.innerWidth, window.innerHeight);

// Append the canvas to the body of the document

document.body.appendChild(renderer.domElement);

document.body.style.backgroundColor = "#201c1c";

const scene = new THREE.Scene();

// Create a camera

const camera = new THREE.PerspectiveCamera(
    75,
    window.innerWidth / window.innerHeight,
    0.1,
    1000
);

const orbit = new OrbitControls(camera, renderer.domElement);

orbit.minPolarAngle = 0;
orbit.maxPolarAngle = Math.PI; // Full range for vertical rotation
//orbit.enableDamping = true; // Smooth rotation
orbit.dampingFactor = 0.05; // Adjust for sensitivity
orbit.enablePan = false; // Disable panning if not needed


const axisHelper = new THREE.AxesHelper(3);

scene.add(axisHelper);

camera.position.set(0, -15, 5)
orbit.update();

const boxGeometry = new THREE.BoxGeometry(0.5, 0.5, 0.5);
const boxMaterial = new THREE.MeshBasicMaterial({color: 0x00FF00});
const box = new THREE.Mesh(boxGeometry, boxMaterial);
scene.add(box);

const sphereGeometry = new THREE.SphereGeometry(2);
const sphereMaterial = new THREE.MeshBasicMaterial({
    color: 0x0000FF,
    wireframe: false
});
const sphere = new THREE.Mesh(sphereGeometry, sphereMaterial);
scene.add(sphere);



//sphere.position.set(1, 1, 3);


const plainGeometry = new THREE.PlaneGeometry(10, 10);
const plainMaterial = new THREE.MeshBasicMaterial({
    color: 0x201c1c,
    side: THREE.DoubleSide
});
const plane = new THREE.Mesh(plainGeometry, plainMaterial);
scene.add(plane);
plane.rotation.x = -0.5*Math.PI

const groundBody = new CANNON.Body({
    //mass: 1,
    shape: new CANNON.Plane(),
    type: CANNON.Body.STATIC
});
world.addBody(groundBody);


const sphereBody = new CANNON.Body({
    mass: 10,
    shape: new CANNON.Sphere(2),
    position: new CANNON.Vec3(0, 0, 5)
    
});

world.addBody(sphereBody);

const boxBody = new CANNON.Body({
    mass: 1,
    shape: new CANNON.Box(new CANNON.Vec3(0.5, 0.5, 0.5)),
    position: new CANNON.Vec3(0, 0, 0)
    
});

world.addBody(boxBody);


/* const gridHelper = new THREE.GridHelper(10, 30);
scene.add(gridHelper); */

const gui = new dat.GUI();

const sphareOptions = {
    sphereColor: '#ffea00',
    wireframe: false
    
};
const boxOptions = {
    boxColor: '#00ff00',
    wireframe: false
};

gui.addColor(sphareOptions, 'sphereColor').onChange(function(e)
{
    sphereMaterial.color.set(e);
});

gui.add(sphareOptions, 'wireframe').onChange(function(e)
{
    sphereMaterial.wireframe = e;
});

gui.addColor(boxOptions, 'boxColor'). onChange(function(e)
{
    boxMaterial.color.set(e);
});

gui.add(boxOptions, 'wireframe').onChange(function(e)
{
    boxMaterial.wireframe = e;
});

var move = 0.1;
var rotate = 100;

var accel = -20;
var deac = 4;

document.onkeydown = function(e)
{
    if(e.keyCode === 68)
    {
        sphereBody.position.x +=move;
        world.gravity(accel, 0, -9.8);
    }
    else if(e.keyCode === 65)
    {
        sphereBody.position.x -=move;
        world.gravity(-accel, 0, -9.8);
    }
    else if(e.keyCode === 83)
    {
        sphereBody.position.y -=move;
        world.gravity(0, -accel, -9.8);
    }
    else if(e.keyCode === 87)
    {
        sphereBody.position.y +=move;
        world.gravity(0, accel, -9.8);
    }
    else
    {
        world.gravity(0, 0, -9.8);
    }
    
}


function animate() {
    world.step(timeStep);

    plane.position.copy(groundBody.position);
    plane.quaternion.copy(groundBody.quaternion);

    sphere.position.copy(sphereBody.position);
    sphere.quaternion.copy(sphereBody.quaternion);

    box.position.copy(boxBody.position);
    box.quaternion.copy(boxBody.quaternion);

    //box.rotation.x += 0.01;
    //box.rotation.y += 0.01;
    renderer.render(scene, camera);
}

renderer.setAnimationLoop(animate);