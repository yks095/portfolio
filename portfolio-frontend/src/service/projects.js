import axios from 'axios';

export function getProjects() {
    return axios.get('http://localhost:8080/api/projects');
}

export function deleteProject(no) {
    return axios.delete('http://localhost:8080/api/projects/' + no);
}

export function editProject(no ,data) {
    return axios.put('http://localhost:8080/api/projects/' + no, data);
}

export function addProject(data) {
    return axios.post('http://localhost:8080/api/projects', data);
}