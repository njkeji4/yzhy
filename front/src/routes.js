import Main from './components/Main.vue';
import HoldView from './components/HoldView.vue';
import Login from './components/Login.vue';
import DeviceList from './components/device/DeviceList.vue';
import DataList from './components/datamgt/DataList.vue';
import BlackList from './components/blacklist/BlackList.vue';
import Settings from './components/settings/SystemSettings.vue';
import MainStat from './components/statistic/Stat.vue';
import MainStat2 from './components/statistic/Stat2.vue';
import Alarm from './components/alarm/AlarmList.vue';
import Usermgt from './components/user/UserManagement.vue';
import versionmgt from './components/version/VersionManagement.vue';
import groupMgt from './components/group/GroupManagement.vue';
import advMgt from './components/adv/AdvManagement.vue';
import advDownload from './components/adv/AdvDownloadResult.vue';

let routes = [  
    {
        path: '/login',
        component: Login,
        name: '',
        hidden: true        
    }, 
    {
        path: '/',
        component: Main,
        name: '',
        children:[
            {
                path:'statis',
                component: MainStat2,
                name:'',
                title:'首页',
                iconCls:'performancemanagement',                
            },
            {
                path: 'device',
                component: DeviceList,
                name:'',
                title:'设备',
                iconCls: 'devicemanagement'
            },
            {
                path: 'data',
                component: HoldView,
                name:'',
                title:'数据',
                iconCls: 'logomanagement',
                children:[
                    {
                        path: 'datamgt',
                        component: DataList,
                        name:'',
                        title:'比对数据',
                        iconCls: 'logomanagement'
                    }, 
                    {
                        path: 'alarm',
                        component: Alarm,
                        name:'',
                        title:'报警数据',
                        iconCls: 'alarmmanagement',                
                    },
                    {
                        path: 'blacklist',
                        component: BlackList,
                        name:'',
                        title:'黑 名 单',
                        iconCls: 'versionmanagement',
                        
                    },       
                ]
            },           
            
            {
                path: 'setting',
                component: HoldView,
                name:'',
                title:'设置',
                iconCls: 'setting',
                admin:1,
                children:[
                    {
                        path: 'configmgt',
                        component: Settings,
                        name:'',
                        title:'配置管理',
                        iconCls: 'setting',
                    },
                    {
                        path: 'versionmgt',
                        component: versionmgt,
                        name:'',
                        title:'版本管理',
                        iconCls: 'TACmanagement',
                        admin:1
                    },
                ]
            },
           
            {
                path: 'u',
                component: HoldView,
                name:'',
                title:'用户',
                iconCls: 'user',
                admin:1,
                children:[
                    {
                        path: 'user',
                        component: Usermgt,
                        name:'',
                        title:'用户管理',
                        iconCls: 'user',
                        admin:1,
                    },
                    {
                        path: 'group',
                        component: groupMgt,
                        name:'',
                        title:'组管理',
                        iconCls: 'group',
                        admin:1
                    },
                ]
            },            
            
            {
                path: 'adv',
                component: HoldView,
                name:'',
                title:'广告',
                iconCls: 'system',
                admin:2,
                children:[
                    {
                        path: 'adv',
                        component: advMgt,
                        name:'',
                        title:'广告管理',
                        iconCls: 'system',
                        admin:2,
                    },
                    {
                        path: 'addtail',
                        component: advDownload,
                        name:'',
                        title:'广告播放',
                        iconCls: 'adminmanagement',
                        admin:2,
                    },
                ]
            },           
            
            {
                path:'*',
                hidden:true,
                redirect:{
                    path: '/statis'
                }
            }
        ]
    },
    {
        path: '*',
        hidden: true,
        redirect: {
            path: '/statis'
        }
    }
   
];
    

export default routes;