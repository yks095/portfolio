import axios from 'axios';

export function getProjects() {
    return axios.get('http://localhost:8080/api/projects');
}