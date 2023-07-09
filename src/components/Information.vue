<template>
  <div id="information">
    <div class="list-wrap" ref="listWrap">
      <div class="scroll-bar" ref="scrollBar"></div>
      <div class="list" ref="list">
        <InforItem v-for="(val,index) in showList" :unreadMessage='val' :key='index' :user="user.account"/>
      </div>
    </div>
  </div>
</template>

<script>
import InforItem from '@/components/InforItem'

export default {
  name: 'information',
  components: { InforItem },
  data () {
    return {
      //未读消息
      unreadMessage: [],
      timer:null
    }
  },
  computed: {
    // 显示的数组，用计算属性计算
    showList () {
      return this.unreadMessage
    }
  },
  props: {
    user: Object
  },
  mounted () {
    // 构造滚动条高度
    this.$refs.scrollBar.style.height = '400px'
    this.getIsRead()
    this.$nextTick(()=>{
      this.timer = setInterval(() => {
        this.getIsRead()
      },5000)
    })
  },
  methods: {
    async getIsRead () {
      console.log(this.user.account)
      this.unreadMessage = []
      const res = await this.$axios({
        url: "http://localhost:8866/login/isRead",
        contentType: "application/x-www-form-urlencoded",
        params: {
          account: this.user.account,
        },
      }).then(res => {
        if (res.data !== "") {
          //未读信息整理
          for (let i = 0; i < res.data.length; i++) {
            let search = this.unreadMessage.findIndex(item=>item.account === res.data[i].speak)
            if (search !== -1) {
              this.unreadMessage[search].count++
              this.unreadMessage[search].say = res.data[i].say
              this.unreadMessage[search].date = res.data[i].date
            }else {
              this.unreadMessage.push({
                account: res.data[i].speak,
                say: res.data[i].say,
                date: res.data[i].date,
                count: 1
              })
            }
          }
        } else {
          console.log("null")
        }
      })
    }
  }
}
</script>

<style scoped>
#information {
  -webkit-app-region: no-drag;
  position: absolute;
  top: 40px;
  width: 100%;
}

.list-wrap{
  position: relative;
  overflow-y: scroll;
  width: 100%;
  box-sizing: border-box;
}
.list{
  position: absolute;
  top: 2px;
  left: 0;
  width: 100%;
}
</style>
