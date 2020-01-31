# view package versions

### 原理


```bash
npm view webpack versions 
yarn info webpack versions
```


### 大致的流程

1. 获取用户光标聚焦的包名
2. 执行命令获取包版本历史
3. 弹窗列表展示
