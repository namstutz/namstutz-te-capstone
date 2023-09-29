import axios from 'axios';

export default {

  getSessions() {
    return axios.get('/sessions')
  },

  getUserSessions(id) {
    return axios.get(`/sessions/user/${id}`)
  },

  getSessionsByBookId(id) {
    return axios.get(`/sessions/${id}`)
  },

  getUserSessionsByBookId(id, userId) {
    return axios.get(`/sessions/${id}/user/${userId}`)
  },

  addSession(session) {
    return axios.post('/sessions', session)
  }
}