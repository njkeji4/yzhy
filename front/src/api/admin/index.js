import axios from 'axios';

import { prefix } from '../apiConfig';

//user
export const login = params => { return axios.post(`${prefix}/user/login`, params); };


export const logout = () => { return axios.post(`${prefix}/user/logout`); };


export const getUserList = () => { return axios.get(`${prefix}/user/list`); };


//device
export const getDeviceList = () => { return axios.get(`${prefix}/device/list?_t=${Date.now()}`); };

export const searchDevice = (param) => { return axios.post(`${prefix}/device/searchDevice`,param); };

export const deleteDevices = (param) => { return axios.post(`${prefix}/device/delete`,param); };

export const updateDevice = (param) => { return axios.post(`${prefix}/device/update`,param); };

//device group
export const getDeviceGroups = () => { return axios.get(`${prefix}/devicegroup/list?_t=${Date.now()}`); };
export const addGroup = (param) => { return axios.post(`${prefix}/devicegroup/add`,param); };
export const editGroup = (param) => { return axios.post(`${prefix}/devicegroup/update`,param); };
export const removeGroup = (param) => { return axios.post(`${prefix}/devicegroup/delete`,param); };


//check data
export const searchCheckData = (param) => { return axios.post(`${prefix}/checkdata/search`,param); };

export const getSpotImg = (id) => { return axios.get(`${prefix}/checkdata/spotimg?id=${id}&_t=${Date.now()}`); };


//user
export const changePassword = (param) => { return axios.post(`${prefix}/user/changepassword`,param); };

export const addUser = (param) => { return axios.post(`${prefix}/user/add`,param); };

export const removeUsers = (param) => { return axios.post(`${prefix}/user/delete`,param); };

//version
export const getVersionList = () => { return axios.get(`${prefix}/version/list?_t=${Date.now()}`); };

export const getExistedVersions = () => { return axios.get(`${prefix}/version/existed?_t=${Date.now()}`); };

export const removeVersion = (param) => { return axios.post(`${prefix}/version/delete`,param); };

export const uploadUrl = `${prefix}/version/upload`;
export const websocketurl = `${prefix}/websocket`;

//setting
export const getSetting = () => { return axios.get(`${prefix}/setting/get?_t=${Date.now()}`); };

export const updateSetting = (param) => { return axios.post(`${prefix}/setting/update`,param); };

//blacklist
export const getBlackList = () => { return axios.get(`${prefix}/blacklist/list?_t=${Date.now()}`); };

export const searchBlackList = (param) => { return axios.post(`${prefix}/blacklist/list`, param); };

export const updateBlacklist = (param) => { return axios.post(`${prefix}/blacklist/add`,param); };

export const removeBlacklist = (param) => { return axios.post(`${prefix}/blacklist/delete`,param); };

export const exportBlackList = () => { return axios.get(`${prefix}/blacklist/export?_t=${Date.now()}`); };

export const uploadBlackListUrl = `${prefix}/blacklist/upload`;

//today
export const getTodayData = () => { return axios.get(`${prefix}/device/todayData?_t=${Date.now()}`); };

//alarm
export const getAlarmList = () => { return axios.get(`${prefix}/alarm/list?_t=${Date.now()}`); };

export const searchAlarmData = (param) => { return axios.post(`${prefix}/alarm/search`,param); };

//adv
export const uploadAdvUrl = `${prefix}/advertisement/upload`;

export const searchAdvList = (param) => { return axios.post(`${prefix}/advertisement/search`,param); };
export const removeAdv = (param) => { return axios.post(`${prefix}/advertisement/delete`,param); };

export const searchAdvResult = (param) => { return axios.post(`${prefix}/advertisement/result`,param); };