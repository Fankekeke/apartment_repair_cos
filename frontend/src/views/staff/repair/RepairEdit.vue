<template>
  <a-modal v-model="show" title="维修任务" @cancel="onClose" :width="1000">
    <template slot="footer">
      <a-button key="back1" @click="repairRequest" v-if="repairData.requestNo == null">
        申请
      </a-button>
      <a-button key="back2" @click="repairDown" type="primary" v-if="repairData.requestNo != null && repairData.auditStatus != null && repairData.auditStatus == 1 && repairData.repairStatus == 1">
        维修完成
      </a-button>
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px" v-if="repairData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>用户姓名：</b>
          {{ repairData.name }}
        </a-col>
        <a-col :span="8"><b>联系电话：</b>
          {{ repairData.phone }}
        </a-col>
        <a-col :span="8"><b>紧急程度：</b>
          <span v-if="repairData.repairLevel == 1">急</span>
          <span v-if="repairData.repairLevel == 2">重</span>
          <span v-if="repairData.repairLevel == 3">轻</span>
          <span v-if="repairData.repairLevel == 4">缓</span>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>维修编号：</b>
          {{ repairData.code }}
        </a-col>
        <a-col :span="16"><b>维修内容：</b>
          {{ repairData.content }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="24">
          <a-upload
            name="avatar"
            action="http://127.0.0.1:9527/file/fileUpload/"
            list-type="picture-card"
            disabled
            :file-list="fileList"
            @preview="handlePreview"
            @change="picHandleChange"
          >
          </a-upload>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>维修类型：</b>
          <span v-if="repairData.repairType == 1">上下水管道</span>
          <span v-if="repairData.repairType == 2">落水管</span>
          <span v-if="repairData.repairType == 3">水箱</span>
          <span v-if="repairData.repairType == 4">天线</span>
          <span v-if="repairData.repairType == 5">供电线路</span>
          <span v-if="repairData.repairType == 6">通讯线路</span>
          <span v-if="repairData.repairType == 7">照明</span>
          <span v-if="repairData.repairType == 8">供气线路</span>
          <span v-if="repairData.repairType == 9">消防设施</span>
        </a-col>
        <a-col :span="8"><b>维修状态：</b>
          <span v-if="repairData.repairStatus == 0">未派修</span>
          <span v-if="repairData.repairStatus == 1">已派修</span>
          <span v-if="repairData.repairStatus == 2">已完成</span>
        </a-col>
        <a-col :span="8"><b>创建时间：</b>
          {{ repairData.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>房屋地址：</b>
          <a-tooltip>
            <template slot="title">
              {{ repairData.address }}
            </template>
            {{ repairData.address.slice(0, 10) }} ...
          </a-tooltip>
        </a-col>
        <a-col :span="8"><b>房间号：</b>
          {{ repairData.number }} 室
        </a-col>
        <a-col :span="8"><b>楼层：</b>
          {{ repairData.floor }} 层
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="repairData.worker !== null">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">维修员工信息</span></a-col>
        <a-col :span="8"><b>员工姓名：</b>
          <a-popover>
            <template slot="content">
              <a-avatar v-if="repairData.workerImage !== null" shape="square" :size="132" icon="user" :src="'http://127.0.0.1:9527/imagesWeb/' + repairData.workerImage.split(',')[0]" />
              <a-avatar v-else shape="square" :size="132" icon="user" />
            </template>
            <a>{{ repairData.workerName !== null ? repairData.workerName : '- -' }}</a>
          </a-popover>
        </a-col>
        <a-col :span="8"><b>联系电话：</b>
          {{ repairData.workerPhone }}
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" :gutter="15">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">选择维修设备</span></a-col>
        <a-col :span="10">
          <a-select v-model="deviceId" style="width: 100%">
            <a-select-option v-for="(item, index) in deviceList" :value="item.id" :key="index">{{ item.deviceName }}</a-select-option>
          </a-select>
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" :gutter="15" v-if="repairData.requestNo == null">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">选择维修物料</span></a-col>
        <a-col :span="24">
          <a-table :columns="columns" :data-source="dataList">
            <template slot="nameShow" slot-scope="text, record">
              <a-select style="width: 100%" @change="handleChange($event, record)">
                <a-select-option v-for="(item, index) in goodsList" :key="index" :value="item.id">{{ item.name }}</a-select-option>
              </a-select>
            </template>
            <template slot="typeShow" slot-scope="text, record">
              <a-input disabled v-model="record.type"></a-input>
            </template>
            <template slot="typeIdShow" slot-scope="text, record">
              <a-select disabled v-model="record.typeId" style="width: 100%">
                <a-select-option v-for="(item, index) in consumableType" :value="item.id" :key="index">{{ item.name }}</a-select-option>
              </a-select>
            </template>
            <template slot="unitShow" slot-scope="text, record">
              <a-input disabled v-model="record.unit"></a-input>
            </template>
            <template slot="amountShow" slot-scope="text, record">
              <a-input-number v-model="record.amount" :min="1" :step="1"/>
            </template>
            <template slot="priceShow" slot-scope="text, record">
              <a-input-number disabled v-model="record.price" :min="1"/>
            </template>
          </a-table>
          <a-button @click="dataAdd" type="primary" ghost size="large" style="margin-top: 10px;width: 100%">
            新增物品
          </a-button>
        </a-col>
      </a-row>
      <a-row style="padding-left: 24px;padding-right: 24px;" :gutter="15" v-if="repairData.requestNo != null">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">物品详情</span></a-col>
        <a-col :span="24">
          <a-table :columns="columns" :data-source="goodsList"></a-table>
        </a-col>
      </a-row>
    </div>
  </a-modal>
</template>

<script>
import {mapState} from 'vuex'
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
export default {
  name: 'RepairEdit',
  props: {
    repairEditVisiable: {
      default: false
    },
    repairData: {
      type: Object
    }
  },
  watch: {
    repairEditVisiable: function (value) {
      if (value && this.repairData.images !== null && this.repairData.images !== '') {
        this.imagesInit(this.repairData.images)
        if (this.repairData.requestNo != null) {
          this.getGoodsByNum(this.repairData.requestNo)
        }
        this.deviceId = this.repairData.deviceId
      }
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.repairEditVisiable
      },
      set: function () {
      }
    },
    columns () {
      return [{
        title: '物品名称',
        dataIndex: 'name',
        width: 200,
        scopedSlots: {customRender: 'nameShow'}
      }, {
        title: '型号',
        dataIndex: 'type',
        scopedSlots: {customRender: 'typeShow'}
      }, {
        title: '数量',
        dataIndex: 'amount',
        scopedSlots: {customRender: 'amountShow'}
      }, {
        title: '所属类型',
        dataIndex: 'typeId',
        width: 200,
        scopedSlots: {customRender: 'typeIdShow'}
      }, {
        title: '单位',
        dataIndex: 'unit',
        scopedSlots: {customRender: 'unitShow'}
      }, {
        title: '单价',
        dataIndex: 'price',
        scopedSlots: {customRender: 'priceShow'}
      }]
    }
  },
  mounted () {
    this.getGoodsList()
    this.getDeviceList()
    this.getConsumableType()
  },
  data () {
    return {
      rowId: null,
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      dataList: [],
      consumableType: [],
      previewVisible: false,
      previewImage: '',
      workerId: null,
      deviceId: null,
      workerList: '',
      deviceList: [],
      goodsList: []
    }
  },
  methods: {
    getGoodsByNum (num) {
      if (num) {
        this.$get('/cos/goods-belong/getGoodsDetailByNum', { num }).then((r) => {
          this.goodsList = r.data.data
        })
      }
    },
    repairDown () {
      this.$get('/cos/repair-info/down', { repairId: this.repairData.id }).then((r) => {
        this.dataList = []
        this.deviceId = null
        this.$emit('success')
      })
    },
    repairRequest () {
      if (this.dataList.length === 0) {
        this.$message.warning('请添加维修耗材信息')
        return false
      }
      this.dataList.forEach(item => {
        item.price = item.price * item.amount
      })
      this.repairData.goodsList = JSON.stringify(this.dataList)
      this.repairData.deviceId = this.deviceId
      this.$post('/cos/repair-info/repairRequest', this.repairData).then((r) => {
        this.dataList = []
        this.deviceId = null
        this.$emit('success')
      })
    },
    handleChange (value, record) {
      if (value) {
        this.goodsList.forEach(e => {
          if (e.id === value) {
            record.name = e.name
            record.type = e.type
            record.amount = 1
            record.typeId = e.typeId
            record.unit = e.unit
            record.price = e.price
            console.log(record)
          }
        })
      }
    },
    getGoodsList () {
      this.$get('/cos/stock-info/queryStockGoods').then((r) => {
        this.goodsList = r.data.data
      })
    },
    dataAdd () {
      this.dataList.push({name: '', type: '', typeId: '', unit: '', amount: '', price: ''})
    },
    getConsumableType () {
      this.$get('/cos/consumable-type/list').then((r) => {
        this.consumableType = r.data.data
      })
    },
    getDeviceList () {
      this.$get('/cos/device-info/list').then((r) => {
        this.deviceList = r.data.data
      })
    },
    getWorkerList () {
      this.$get('/cos/worker-info/list', { type: 2 }).then((r) => {
        this.workerList = r.data.data
      })
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      let data = Object.assign(this.repairData, {})
      if (this.workerId !== null) {
        data.worker = this.workerId
        this.$put('/cos/repair-info', data).then((r) => {
          this.workerId = null
          this.$emit('success')
        }).catch(() => {
          this.loading = false
        })
      } else {
        this.$message.warning('请选择工作人员！')
      }
    }
  }
}
</script>

<style scoped>

</style>
