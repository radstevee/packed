# Assets
Packed supports various asset resolution strategies:

## Resource
The `ResourceAssetResolutionStrategy` takes in the class of which the resources should be resolved with.

It is dead simple: It resolves assets based of the files in `assets` in a JARs `resources`. Only limitation
is that it does not copy over `pack.png` files, which can be done manually if you wish.

## Source Directory
The `SourceDirectoryAssetResolutionStrategy` takes in the source directory to be used for
resolving assets. Copies over ALL files into the packs output directory.

## Git
The `GitAssetResolutionStrategy` takes in a `KGit` instance, which you can retrieve using `KGit.cloneRepository`.

It copies all files from the cloned directory to the packs output directory.

::: danger
The `.git` directory is not excluded from copying. This can potentially leak information.

If you wish to remove it manually, you can use a [Packed plugin](/plugins) or use a subdirectory.
:::

## Custom
If you wish to use a custom asset resolution strategy, you can implement the `AssetResolutionStrategy` interface along with
the `getAsset(relativePath: Path): File?` and `copyAssets(targetFile: File)` functions.