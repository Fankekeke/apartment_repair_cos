<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="房间地址"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.address"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="购房人"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.buyer"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="所属社区"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.community"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="房间性质"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-select v-model="queryParams.nature" allowClear>
                  <a-select-option value="1">住宅楼房</a-select-option>
                  <a-select-option value="2">社区用处</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </div>
          <span style="float: right; margin-top: 3px;">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
          </span>
        </a-row>
      </a-form>
    </div>
    <div>
      <div class="operator">
<!--        <a-button type="primary" ghost @click="add">新增</a-button>-->
<!--        <a-button @click="batchDelete">删除</a-button>-->
      </div>
      <!-- 表格区域 -->
      <a-table ref="TableInfo"
               :columns="columns"
               :rowKey="record => record.id"
               :dataSource="dataSource"
               :pagination="pagination"
               :loading="loading"
               :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
               :scroll="{ x: 900 }"
               @change="handleTableChange">
        <template slot="addressShow" slot-scope="text, record">
          <template>
            <a-tooltip>
              <template slot="title">
                {{ record.address }}
              </template>
              {{ record.address.slice(0, 8) }} ...
            </a-tooltip>
          </template>
        </template>
        <template slot="contentShow" slot-scope="text, record">
          <template>
            <a-tooltip>
              <template slot="title">
                {{ record.content }}
              </template>
              {{ record.content.slice(0, 30) }} ...
            </a-tooltip>
          </template>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-icon type="reconciliation" @click="view(record)" title="查 看" style="margin-right: 15px"></a-icon>
<!--          <a-icon type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="edit(record)" title="修 改"></a-icon>-->
        </template>
      </a-table>
    </div>
    <houses-add
      v-if="housesAdd.visiable"
      @close="handleHousesAddClose"
      @success="handleHousesAddSuccess"
      :housesAddVisiable="housesAdd.visiable">
    </houses-add>
    <houses-edit
      ref="housesEdit"
      @close="handleHousesEditClose"
      @success="handleHousesEditSuccess"
      :housesEditVisiable="housesEdit.visiable">
    </houses-edit>
    <houses-view
      @close="handleHousesViewClose"
      :housesShow="housesView.visiable"
      :housesData="housesView.data">
    </houses-view>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import HousesAdd from './HousesAdd'
import HousesEdit from './HousesEdit'
import HousesView from './HousesView'
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')

export default {
  name: 'houses',
  components: {HousesAdd, HousesEdit, HousesView, RangeDate},
  data () {
    return {
      advanced: false,
      housesAdd: {
        visiable: false
      },
      housesEdit: {
        visiable: false
      },
      housesView: {
        visiable: false,
        data: null
      },
      queryParams: {},
      filteredInfo: null,
      sortedInfo: null,
      paginationInfo: null,
      dataSource: [],
      selectedRowKeys: [],
      loading: false,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      userList: []
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    columns () {
      return [{
        title: '房屋地址',
        dataIndex: 'address',
        ellipsis: true,
        scopedSlots: {customRender: 'addressShow'}
      }, {
        title: '所属楼宇',
        ellipsis: true,
        dataIndex: 'buildName'
      }, {
        title: '房号',
        dataIndex: 'number',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text + '室'
          } else {
            return '- -'
          }
        }
      }, {
        title: '楼层',
        dataIndex: 'floor',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text + '层'
          } else {
            return '- -'
          }
        }
      }, {
        title: '使用面积',
        dataIndex: 'usageArea',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text + '㎡'
          } else {
            return '- -'
          }
        }
      }, {
        title: '建筑面积',
        dataIndex: 'surfaceArea',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text + '㎡'
          } else {
            return '- -'
          }
        }
      }, {
        title: '间数',
        dataIndex: 'rooms',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text + '间'
          } else {
            return '- -'
          }
        }
      }, {
        title: '房间性质',
        dataIndex: 'nature',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return <a-tag>住宅楼房</a-tag>
            case 2:
              return <a-tag>社区用处</a-tag>
            default:
              return '- -'
          }
        }
      }, {
        title: '购房人',
        dataIndex: 'buyer'
      }, {
        title: '管理员',
        dataIndex: 'workerName'
      }, {
        title: '创建时间',
        dataIndex: 'createDate',
        ellipsis: true,
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: {customRender: 'operation'}
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    view (row) {
      this.housesView.data = row
      this.housesView.visiable = true
    },
    handleHousesViewClose () {
      this.housesView.visiable = false
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    add () {
      this.housesAdd.visiable = true
    },
    handleHousesAddClose () {
      this.housesAdd.visiable = false
    },
    handleHousesAddSuccess () {
      this.housesAdd.visiable = false
      this.$message.success('新增房屋成功')
      this.search()
    },
    edit (record) {
      this.$refs.housesEdit.setFormValues(record)
      this.housesEdit.visiable = true
    },
    handleHousesEditClose () {
      this.housesEdit.visiable = false
    },
    handleHousesEditSuccess () {
      this.housesEdit.visiable = false
      this.$message.success('修改房屋成功')
      this.search()
    },
    handleDeptChange (value) {
      this.queryParams.deptId = value || ''
    },
    batchDelete () {
      if (!this.selectedRowKeys.length) {
        this.$message.warning('请选择需要删除的记录')
        return
      }
      let that = this
      this.$confirm({
        title: '确定删除所选中的记录?',
        content: '当您点击确定按钮后，这些记录将会被彻底删除',
        centered: true,
        onOk () {
          let ids = that.selectedRowKeys.join(',')
          that.$delete('/cos/houses-info/' + ids).then(() => {
            that.$message.success('删除成功')
            that.selectedRowKeys = []
            that.search()
          })
        },
        onCancel () {
          that.selectedRowKeys = []
        }
      })
    },
    search () {
      let {sortedInfo, filteredInfo} = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.fetch({
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams,
        ...filteredInfo
      })
    },
    reset () {
      // 取消选中
      this.selectedRowKeys = []
      // 重置分页
      this.$refs.TableInfo.pagination.current = this.pagination.defaultCurrent
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
        this.paginationInfo.pageSize = this.pagination.defaultPageSize
      }
      // 重置列过滤器规则
      this.filteredInfo = null
      // 重置列排序规则
      this.sortedInfo = null
      // 重置查询参数
      this.queryParams = {}
      this.fetch()
    },
    handleTableChange (pagination, filters, sorter) {
      // 将这三个参数赋值给Vue data，用于后续使用
      this.paginationInfo = pagination
      this.filteredInfo = filters
      this.sortedInfo = sorter

      this.fetch({
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...this.queryParams,
        ...filters
      })
    },
    fetch (params = {}) {
      // 显示loading
      this.loading = true
      if (this.paginationInfo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfo.pagination.current = this.paginationInfo.current
        this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize
        params.size = this.paginationInfo.pageSize
        params.current = this.paginationInfo.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.size = this.pagination.defaultPageSize
        params.current = this.pagination.defaultCurrent
      }
      if (params.nature === undefined) {
        delete params.nature
      }
      params.userId = this.currentUser.userId
      this.$get('/cos/houses-info/page', {
        ...params
      }).then((r) => {
        let data = r.data.data
        const pagination = {...this.pagination}
        pagination.total = data.total
        this.dataSource = data.records
        this.pagination = pagination
        // 数据加载完毕，关闭loading
        this.loading = false
      })
    }
  },
  watch: {}
}
</script>
<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
