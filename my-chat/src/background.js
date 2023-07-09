import { app, protocol, BrowserWindow } from 'electron'
import { createProtocol } from 'vue-cli-plugin-electron-builder/lib'
import installExtension, { VUEJS_DEVTOOLS } from 'electron-devtools-installer'
const isDevelopment = process.env.NODE_ENV !== 'production'

// Scheme must be registered before the app is ready
protocol.registerSchemesAsPrivileged([
  { scheme: 'app', privileges: { secure: true, standard: true } }
])

async function createWindow() {
  // Create the browser window.
  let chatList
  let chatWindow
  let contactWindow
  let win = new BrowserWindow({
    width: 425,
    height: 325,
    //图标
    icon: 'src/assets/desktop-icon.png',
    //隐藏导航栏
    frame: false,
    //窗口不可调整
    resizable: false,
    //置顶
    alwaysOnTop: true,
    webPreferences: {
      // Use pluginOptions.nodeIntegration, leave this alone
      // See nklayman.github.io/vue-cli-plugin-electron-builder/guide/security.html#node-integration for more info
      // nodeIntegration: process.env.ELECTRON_NODE_INTEGRATION,
      // contextIsolation: !process.env.ELECTRON_NODE_INTEGRATION,
      //关闭开发者工具
      devTools:false,
      nodeIntegration: true,
      contextIsolation: false,
      preload: 'E:\\develop\\hello\\src\\chat\\my-chat\\preload.js'
    }
  })

  if (process.env.WEBPACK_DEV_SERVER_URL) {
    // Load the url of the dev server if in development mode
    await win.loadURL(process.env.WEBPACK_DEV_SERVER_URL)
    if (!process.env.IS_TEST) win.webContents.openDevTools()
  } else {
    createProtocol('app')
    // Load the index.html when not in development
    await win.loadURL('http://localhost:8081/#/')
  }
  const { ipcMain } = require('electron')
  //接受关闭窗口命令
  ipcMain.on('window-close',function (){
    win.close();
  })
  //接受刷新窗口命令
  ipcMain.on('window-refresh',function (){
    chatList.reload();
  })
  //接受最小化窗口命令
  ipcMain.on('window-mini', function (){
    win.minimize();
  })
  //接受新窗口list命令
  ipcMain.on('new-window', (event, a) =>{
    // win.close();
    chatList = new BrowserWindow({
      width: 300,
      height: 550,
      //图标
      icon: 'src/assets/desktop-icon.png',
      //隐藏导航栏
      frame: false,
      //窗口不可调整
      resizable:false,
      //置顶
      alwaysOnTop: true,
      //优化加载过程出现白屏
      show:false,
      webPreferences: {
        //关闭开发者工具
        devTools:false,
        nodeIntegration: true,
        contextIsolation: false,
        enableRemoteModule: true,
        preload: 'E:\\develop\\hello\\src\\chat\\my-chat\\preload.js'
      }
    })
    require("@electron/remote/main").enable(chatList.webContents);
    chatList.setPosition(1200,0)
    // chatList.webContents.openDevTools()
    chatList.loadURL(`http://localhost:8081/#/chatList?user=${a}`)
    chatList.show();
  })
  //接受新窗口chatWindow命令
  ipcMain.on('new-chatWindow', (event, a) => {
    chatWindow = new BrowserWindow({
      width: 1000,
      height: 600,
      //图标
      icon: 'E:\\develop\\hello\\src\\chat\\my-chat\\src\\assets\\img.png',
      //隐藏导航栏
      frame: false,
      //窗口不可调整
      resizable: true,
      //优化加载过程出现白屏
      show:false,
      webPreferences: {
        //关闭开发者工具
        devTools:false,
        enableRemoteModule: true,
        nodeIntegration: true,
        contextIsolation: false,
        preload: 'E:\\develop\\hello\\src\\chat\\my-chat\\preload.js'
      }
    })
    require("@electron/remote/main").enable(chatWindow.webContents);
    // chatWindow.webContents.openDevTools()
    chatWindow.loadURL(`http://localhost:8081/#/chatWindow?user=${a.user}&contact=${a.contact}`)
    chatWindow.show();
  })
  //接受新窗口contactWindow命令
  ipcMain.on('new-contactWindow', (event, a) => {
    contactWindow = new BrowserWindow({
      width: 500,
      height: 300,
      //图标
      icon: 'E:\\develop\\hello\\src\\chat\\my-chat\\src\\assets\\img.png',
      //隐藏导航栏
      frame: false,
      //窗口不可调整
      resizable: false,
      //优化加载过程出现白屏
      show:false,
      webPreferences: {
        //关闭开发者工具
        devTools:false,
        enableRemoteModule: true,
        nodeIntegration: true,
        contextIsolation: false,
        preload: 'E:\\develop\\hello\\src\\chat\\my-chat\\preload.js'
      }
    })
    require("@electron/remote/main").enable(contactWindow.webContents);
    // contactWindow.webContents.openDevTools()
    contactWindow.loadURL(`http://localhost:8081/#/newContact?user=${a}`)
    contactWindow.show();
  })
}

// Quit when all windows are closed.
app.on('window-all-closed', () => {
  // On macOS it is common for applications and their menu bar
  // to stay active until the user quits explicitly with Cmd + Q
  if (process.platform !== 'darwin') {
    app.quit()
  }
})

app.on('activate', () => {
  // On macOS it's common to re-create a window in the app when the
  // dock icon is clicked and there are no other windows open.
  if (BrowserWindow.getAllWindows().length === 0) createWindow()
})

// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.
app.on('ready', async () => {
  if (isDevelopment && !process.env.IS_TEST) {
    // Install Vue Devtools
    try {
      await installExtension(VUEJS_DEVTOOLS)
    } catch (e) {
      console.error('Vue Devtools failed to install:', e.toString())
    }
  }
  await createWindow()
  // 隐藏菜单栏
  const { Menu } = require('electron');
  require("@electron/remote/main").initialize();
  Menu.setApplicationMenu(null);
  // hide menu for Mac
  if (process.platform !== 'darwin') {
    app.dock.hide();
  }
})

// Exit cleanly on request from parent process in development mode.
if (isDevelopment) {
  if (process.platform === 'win32') {
    process.on('message', (data) => {
      if (data === 'graceful-exit') {
        app.quit()
      }
    })
  } else {
    process.on('SIGTERM', () => {
      app.quit()
    })
  }
}

