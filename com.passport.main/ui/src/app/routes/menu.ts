export const menu = [
  {
    text: '分析',
    heading: true
  },
  {
    text: '仪表盘',
    link: '/dashboard/v1'
  },
  {
    text: '功能',
    heading: true
  },
  {
    text: '系统管理',
    link: '/system',
    icon: 'fa fa-gears',
    submenu: [{
      text: '运营用户管理',
      link: '/system/admin/list'
    },
    {
      text: '运营角色管理',
      link: '/system/role/list'
    },
    {
      text: '客户管理',
      link: '/system/user/list'
    },
    {
      text: '代理商管理',
      link: '/system/proxy/list'
    },
    {
      text: '服务器配置管理',
      link: '/system/server/list'
    },
      {
        text: '软件管理',
        link: '/system/software/list'
      }
    ]
  }
];
