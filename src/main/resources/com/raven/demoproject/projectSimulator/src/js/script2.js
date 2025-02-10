import * as THREE from 'three';
import * as YUKA from 'yuka';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader.js';
import * as dat from 'dat.gui';
import * as CANNON from 'cannon-es';


const monkeyUrl = new URL('../assets/doggo2.glb', import.meta.url);
const mapUrl = new URL('../assets/uiuMap.glb', import.meta.url)

const vehicleGeometry = new THREE.ConeGeometry(0.1, 0.5, 8);
vehicleGeometry.rotateX(Math.PI*0.5);
const vehicleMaterial = new THREE.MeshNormalMaterial();
const vehicleMesh = new THREE.Mesh(vehicleGeometry, vehicleMaterial);
vehicleMesh.matrixAutoUpdate = false;

const vehicle = new YUKA.Vehicle();
vehicle.setRenderComponent(vehicleMesh, sync);

function sync(entity, renderComponent)
{
    renderComponent.matrix.copy(entity.worldMatrix);
}

let pathX = 0, pathY = 0, pathZ = 0;
let movePath = 0.1;


const path = new YUKA.Path();
let ek
if(1)
{
    /* path.add( new YUKA.Vector3(-4, 0, 4));
    path.add( new YUKA.Vector3(-6, 0, 0));
    path.add( new YUKA.Vector3(-4, 0, -4));
    path.add( new YUKA.Vector3(-2, 0, -2));
    path.add( new YUKA.Vector3(4, 0, -4));
    path.add( new YUKA.Vector3(6, 0, 0));
    path.add( new YUKA.Vector3(4, 0, 4));
    path.add( new YUKA.Vector3(0, 0, 6)); */
}
/* document.onkeydown = function(e)
{
    if(e.keyCode === 68)
        {
            pathX += movePath
            sphereBody.position.x +=move;
            
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
//path.add(new YUKA.Vector3()) */
path.add( new YUKA.Vector3(pathX, pathY, 0));


document.onkeydown = function(e)
{
    if(e.keyCode === 68)
        {
            ek = e;
            pathX +=movePath;
            vehicle.maxSpeed = 1;
            path.add( new YUKA.Vector3(pathX, pathY, pathZ));
            
            
            //path.add( new YUKA.Vector3(-5, 0, 4));
            
        }
    
}
document.onkeyup = function(e)
{
    pathX=vehPosX;
    path.add( new YUKA.Vector3(pathX, pathY, pathZ));
    vehicle.maxSpeed = 0.01;
}

vehicle.position.copy(path.current());

const followPathBehaviour = new YUKA.FollowPathBehavior(path, 0.5);

vehicle.steering.add(followPathBehaviour);

const entityManager = new YUKA.EntityManager();
entityManager.add(vehicle);



const positions = [];

for(let i = 0; i<path._waypoints.length; i++)
{
    const waypoints = path._waypoints[i];
    positions.push(waypoints.x, waypoints.y, waypoints.z);
}

const lineGeometry = new THREE.BufferGeometry();
lineGeometry.setAttribute('position', new THREE.Float32BufferAttribute(positions, 3));

const lineMaterials = new THREE.LineBasicMaterial({color: 0x15F2FA, linewidth: 500});
const lines = new THREE.LineLoop(lineGeometry, lineMaterials);


const time = new YUKA.Time();

function renderScenes() {
    var renderer = new THREE.WebGLRenderer();
    renderer.setSize(window.innerWidth, window.innerHeight);
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

function orbitCreation(camera, renderer) {
    var orbit = new OrbitControls(camera, renderer.domElement);

    orbit.minPolarAngle = 0;
    orbit.maxPolarAngle = Math.PI; // Full range for vertical rotation
    orbit.enableDamping = true; // Smooth rotation
    orbit.dampingFactor = 0.05; // Adjust for sensitivity
    orbit.enablePan = false; // Disable panning if not needed

    return orbit;
}

function axisCreation() {
    var axisHelper = new THREE.AxesHelper(3);
    return axisHelper;
}

function planeCreator() {
    var plainGeometry = new THREE.PlaneGeometry(500, 500);
    var plainMaterial = new THREE.MeshBasicMaterial({
        color: 0x444444, // Lighter gray for better visibility
        side: THREE.DoubleSide
    });
    var plane = new THREE.Mesh(plainGeometry, plainMaterial);
    plane.rotation.x = -0.5 * Math.PI;

    return { plainGeometry, plainMaterial, plane };
}

// Lighting function
function addLighting(scene) {
    const light = new THREE.AmbientLight(0xffffff, 1); // Soft white light
    scene.add(light);

    const directionalLight = new THREE.DirectionalLight(0xffffff, 0.5);
    directionalLight.position.set(10, 10, 10);
    scene.add(directionalLight);
}

function adderAndUpdater() {
    scene.add(plane);
    scene.add(axisHelper);
    scene.add(vehicleMesh);
    scene.add(lines);
}


var { renderer, scene } = renderScenes();
var camera = cameraCreation();
var orbit = orbitCreation(camera, renderer);
var axisHelper = axisCreation();
var { plainGeometry, plainMaterial, plane } = planeCreator();

const assetLoader = new GLTFLoader();

let mixer=0;


if (0) {
    assetLoader.load(monkeyUrl.href, function (gltf) {
        const dog = gltf.scene;
        scene.add(dog);
        mixer = new THREE.AnimationMixer(dog);
    
        const clips = gltf.animations;
        const clip = THREE.AnimationClip.findByName(clips, 'HeadAction');
        const action = mixer.clipAction(clip);
        action.play();
    
    
    }, undefined, function (error) {
        console.error(error);
    });
    assetLoader.load(mapUrl.href, function (gltf) {
        const map = gltf.scene;

        map.position.x = 10;
        scene.add(map);


    }, undefined, function (error) {
        console.error(error);
    }
    );
}


addLighting(scene);
adderAndUpdater();


camera.position.set(5, 5, 5);

const clock = new THREE.Clock();
let vehPosX;
function animate() {
    const delta = time.update().getDelta();
    entityManager.update(delta);
    if (mixer)
        mixer.update(clock.getDelta());
    orbit.update(); // Smooth damping
    renderer.render(scene, camera);
    console.log(vehicle.position.x);
    console.log(document.onkeydown);
    vehPosX = vehicle.position.x
}

renderer.setAnimationLoop(animate);
