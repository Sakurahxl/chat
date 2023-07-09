<template>
  <div id="logon">
    <h1 id="logon-title">欢迎注册mychat</h1>
    <el-input class="logon-input1" v-model="account" placeholder="请输入账号">
      <i slot="prefix" class="el-input__icon el-icon-chat-round"></i>
    </el-input>
    <el-input type="password" class="logon-input2" v-model="password" placeholder="请输入密码">
      <i slot="prefix" class="el-input__icon el-icon-lock"></i>
    </el-input>
    <el-input type="password" class="logon-input3" v-model="confirmPass" placeholder="请再次输入密码">
      <i slot="prefix" class="el-input__icon el-icon-lock"></i>
    </el-input>
    <el-button type="primary" id="logon-button" @click="createUser">注     册</el-button>
    <div id="logon-my-chat">
      <img :src="logo" alt="logo" id="logon-my-chat-icon">
      <p id="logon-my-chat-name">MYChat</p>
    </div>
    <h3 class="msg" ref="msg"></h3>
    <img class="left-img" :src="img1"/>
    <img class="right-img" :src="img2"/>
  </div>
</template>

<script>
export default {
  name: 'logon',
  data () {
    return {
      logo: require('@/assets/desktop-icon.png'),
      account: '',
      password: '',
      confirmPass: '',
      result: '',
      img1: require('@/assets/1215.jpg'),
      img2: require('@/assets/1151.jpg')
    }
  },
  methods: {
    createUser() {
      this.formValidation()
    },

    //表单验证
    formValidation() {
      if (this.account !== ''&& this.password !== '' && this.confirmPass !== '') {
        if(this.password === this.confirmPass) {
          this.getData()
          this.account = ''
          this.password = ''
          this.confirmPass = ''
          this.$refs.msg.innerText = ""
        }else {
          this.$refs.msg.innerText = "两次输入密码不一致"
        }
      }else {
        this.$refs.msg.innerText = "数据不能为空"
      }
    },

    //查看数据是否存在，若不存在，则注册成功
    async getData() {
      const res = this.$axios({
        url:"http://localhost:8866/logon",
        contentType: "application/x-www-form-urlencoded",
        params:{
          account: this.account,
          password: this.password
        },
      }).then(res => {
        this.result = res.data
        if (this.result === 'success') {
          alert('注册成功！')
        }else {
          this.$refs.msg.innerText = "该用户已注册，请换一个用户注册"
        }
      })
    }
  }
}
</script>

<style scoped>
#logon {
  width: 1000px;
  margin:0px auto;
  background-color: aquamarine;
  height: 757px;
}

#logon-title {
  position: absolute;
  left: 41%;
  top: 10%;
}

.logon-input1 {
  position: absolute;
  width: 400px;
  top: 27%;
  left: 37%;
}

.logon-input2 {
  position: absolute;
  width: 400px;
  top: 40%;
  left: 37%;
}


.logon-input3 {
  position: absolute;
  width: 400px;
  top: 53%;
  left: 37%;
}

#logon-button {
  position: absolute;
  text-align: center;
  line-height: 0;
  height: 50px;
  width: 400px;
  top: 70%;
  left: 37%;
}

#logon-my-chat {
  position: absolute;
  left: 18%;
  top: 5px;
}

#logon-my-chat-icon {
  width: 30px;
}

#logon-my-chat-name {
  position: absolute;
  left: 40px;
  top: -10px;
}

.msg {
  position: absolute;
  color: red;
  top: 60%;
  left: 37%;
}

.left-img {
  position: absolute;
  height: 100%;
  width: 50%;
  left: 0;
  z-index: -100;
}

.right-img{
  position: absolute;
  height: 100%;
  width: 50%;
  right: 0;
  z-index: -100;
}
</style>
