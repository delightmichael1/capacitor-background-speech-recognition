# speech-to-text

A capacitor plugin for speech to text that can work in the background

## Install

```bash
npm i @deldev/speech-to-text
npx cap sync
```

## API

<docgen-index>

* [`startListening(...)`](#startlistening)
* [`stopListening()`](#stoplistening)
* [`isAvailable()`](#isavailable)
* [`requestPermissions()`](#requestpermissions)
* [`checkPermissions()`](#checkpermissions)
* [`addListener('onResults', ...)`](#addlisteneronresults-)
* [`removeAllListeners()`](#removealllisteners)
* [`addListener('onError', ...)`](#addlisteneronerror-)
* [`addListener('onPermissionsResult', ...)`](#addlisteneronpermissionsresult-)
* [`removeAllListeners()`](#removealllisteners)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### startListening(...)

```typescript
startListening(options?: { language?: string | undefined; } | undefined) => Promise<void>
```

| Param         | Type                                |
| ------------- | ----------------------------------- |
| **`options`** | <code>{ language?: string; }</code> |

--------------------


### stopListening()

```typescript
stopListening() => Promise<void>
```

--------------------


### isAvailable()

```typescript
isAvailable() => Promise<{ available: boolean; }>
```

**Returns:** <code>Promise&lt;{ available: boolean; }&gt;</code>

--------------------


### requestPermissions()

```typescript
requestPermissions() => Promise<{ recordAudio: boolean; foregroundService: boolean; }>
```

**Returns:** <code>Promise&lt;{ recordAudio: boolean; foregroundService: boolean; }&gt;</code>

--------------------


### checkPermissions()

```typescript
checkPermissions() => Promise<{ recordAudio: boolean; foregroundService: boolean; }>
```

**Returns:** <code>Promise&lt;{ recordAudio: boolean; foregroundService: boolean; }&gt;</code>

--------------------


### addListener('onResults', ...)

```typescript
addListener(eventName: 'onResults', listenerFunc: (result: { transcript: string; }) => void) => Promise<PluginListenerHandle>
```

| Param              | Type                                                      |
| ------------------ | --------------------------------------------------------- |
| **`eventName`**    | <code>'onResults'</code>                                  |
| **`listenerFunc`** | <code>(result: { transcript: string; }) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### removeAllListeners()

```typescript
removeAllListeners() => Promise<void>
```

--------------------


### addListener('onError', ...)

```typescript
addListener(eventName: 'onError', listenerFunc: (error: { message: string; }) => void) => Promise<PluginListenerHandle>
```

| Param              | Type                                                  |
| ------------------ | ----------------------------------------------------- |
| **`eventName`**    | <code>'onError'</code>                                |
| **`listenerFunc`** | <code>(error: { message: string; }) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### addListener('onPermissionsResult', ...)

```typescript
addListener(eventName: 'onPermissionsResult', listenerFunc: (result: { recordAudio: boolean; foregroundService: boolean; }) => void) => Promise<PluginListenerHandle>
```

| Param              | Type                                                                                    |
| ------------------ | --------------------------------------------------------------------------------------- |
| **`eventName`**    | <code>'onPermissionsResult'</code>                                                      |
| **`listenerFunc`** | <code>(result: { recordAudio: boolean; foregroundService: boolean; }) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### removeAllListeners()

```typescript
removeAllListeners() => Promise<void>
```

--------------------


### Interfaces


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |

</docgen-api>
